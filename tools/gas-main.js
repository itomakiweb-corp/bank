/**
 * プロジェクト全般定期実行Google Apps Script
 *
 * @see https://drive.google.com/open?id=15wOLmTL8HGkWKhiFjLSvUCQJPrq2jeAo-4JRuRN_R96NJSUI2UMGIB_v
 */


// ## main（triggersなどからの実行を想定）

/**
 * GitHubに、先週のまとめと今週の予定を確認するクエストを登録する
 * Slackに、先週のまとめと今週の予定を送信する
 */
function postDoneAndTodoToSlack() {
  const configs = getConfigs()

  const dueDate = new Date()
  const dueDateFormat = getDate8(dueDate)
  const title = '先週のまとめと今週と来週の予定を確認して検討する（${dueDateFormat}）'
    .replace('${dueDateFormat}', dueDateFormat)
  log(title)

  const fetchDoneMilestone = fetchMilestonesFromGithub(['CLOSED'], 'DESC', 1)
  const fetchTodoMilestone = fetchMilestonesFromGithub(['OPEN'], 'ASC', 1)
  const doneMilestone = fetchDoneMilestone.data.repository.milestones.edges[0].node
  const todoMilestone = fetchTodoMilestone.data.repository.milestones.edges[0].node
  log(doneMilestone)
  log(todoMilestone)

  const doneUrl = doneMilestone.url + '?closed=1'
  const todoUrl = todoMilestone.url
  const body = '\
- [ ] 先週やった内容\n\
  - ${doneUrl}\n\
- [ ] 今週やる予定\n\
  - ${todoUrl}\n\
  - Milestoneをきちんと設定する\n\
  - 今週の作業が完了したら、Milestoneをクローズする\n\
- 既に着手できるクエスト\n\
  - ${configs.URL_QUEST_READY}\n\
- クエスト一覧\n\
  - ${configs.URL_QUEST_LIST}\n\
- クエスト追加\n\
  - ${configs.URL_QUEST_NEW}\n\
- 作業の流れ\n\
  - ${configs.URL_MAIN}\n\
- 以下から自動送信\n\
  - ${configs.URL_GAS}\n\
  - ${configs.URL_GAS_SOURCE}\n\
'
    .replace('${configs.URL_MAIN}', configs.URL_MAIN)
    .replace('${configs.URL_QUEST_LIST}', configs.URL_QUEST_LIST)
    .replace('${doneUrl}', doneUrl)
    .replace('${todoUrl}', todoUrl)
    .replace('${configs.URL_QUEST_READY}', configs.URL_QUEST_READY)
    .replace('${configs.URL_QUEST_NEW}', configs.URL_QUEST_NEW)
    .replace('${configs.URL_GAS}', configs.URL_GAS)
    .replace('${configs.URL_GAS_SOURCE}', configs.URL_GAS_SOURCE)
  log(body)

  const input = {
    repositoryId: configs.GITHUB_REPOSITORY_ID,
    title: title,
    body: body,
    assigneeIds: configs.GITHUB_TODO_ASSIGNEE_IDS,
    labelIds: configs.GITHUB_TODO_LABEL_IDS,
    projectIds: configs.GITHUB_TODO_PROJECT_IDS,
    milestoneId: todoMilestone.id,
  }
  const doneAndTodoQuest = postIssueToGithub(input)
  log(doneAndTodoQuest)

  const slackBody = body + '  - ' + doneAndTodoQuest.data.createIssue.issue.url
  const doneAndTodoMessage = postMessageToSlack(slackBody)
  log(doneAndTodoMessage)
}

/**
 * postDoneAndTodoToSlackのオフライン版
 * 第2、4土曜日にのみ実行
 * GitHubに、先週のまとめと今週の予定を確認するクエストを登録する
 * Slackに、先週のまとめと今週の予定を送信する
 */
function postDoneAndTodoToSlackForOffline() {
  const configs = getConfigs()

  const dueDate = new Date()
  const isTargetDay = dueDate.getDay() === configs.TARGET_DAY // 曜日一致
  const weekOfMonth = Math.ceil(dueDate.getDate() / 7)
  const isTargetWeeks = configs.TARGET_WEEKS.indexOf(weekOfMonth) !== -1
  const isTargetDate = isTargetDay && isTargetWeeks
  if (!isTargetDate) {
    const message = {
      message: 'today is not target date',
      isTargetDay: isTargetDay,
      weekOfMonth: weekOfMonth,
      isTargetWeeks: isTargetWeeks,
      isTargetDate: isTargetDate,
    }
    log(message)

    return
  }

  const dueDateFormat = getDate8(dueDate)
  const title = '木曜日のまとめと土曜日の予定を確認して検討する（${dueDateFormat}）'
    .replace('${dueDateFormat}', dueDateFormat)
  log(title)

  const fetchDoneMilestone = fetchMilestonesFromGithub(['CLOSED'], 'DESC', 1)
  const fetchTodoMilestone = fetchMilestonesFromGithub(['OPEN'], 'ASC', 1)
  const doneMilestone = fetchDoneMilestone.data.repository.milestones.edges[0].node
  const todoMilestone = fetchTodoMilestone.data.repository.milestones.edges[0].node
  log(doneMilestone)
  log(todoMilestone)

  const doneUrl = doneMilestone.url + '?closed=1'
  const todoUrl = todoMilestone.url
  const body = '\
- [ ] クエスト一覧\n\
  - ${configs.URL_QUEST_LIST}\n\
- [ ] 木曜日のまとめ\n\
  - ${doneUrl}\n\
- [ ] 土曜日の予定\n\
  - ${todoUrl}\n\
  - Milestoneをきちんと設定する\n\
- [ ] 既に着手できるクエスト\n\
  - ${configs.URL_QUEST_READY}\n\
- [ ] クエスト追加\n\
  - ${configs.URL_QUEST_NEW}\n\
- [ ] 作業の流れ\n\
  - ${configs.URL_MAIN}\n\
- [ ] 以下から自動送信\n\
  - ${configs.URL_GAS}\n\
  - ${configs.URL_GAS_SOURCE}\n\
'
    .replace('${configs.URL_MAIN}', configs.URL_MAIN)
    .replace('${configs.URL_QUEST_LIST}', configs.URL_QUEST_LIST)
    .replace('${doneUrl}', doneUrl)
    .replace('${todoUrl}', todoUrl)
    .replace('${configs.URL_QUEST_READY}', configs.URL_QUEST_READY)
    .replace('${configs.URL_QUEST_NEW}', configs.URL_QUEST_NEW)
    .replace('${configs.URL_GAS}', configs.URL_GAS)
    .replace('${configs.URL_GAS_SOURCE}', configs.URL_GAS_SOURCE)
  log(body)

  const input = {
    repositoryId: configs.GITHUB_REPOSITORY_ID,
    title: title,
    body: body,
    assigneeIds: configs.GITHUB_TODO_ASSIGNEE_IDS,
    labelIds: configs.GITHUB_TODO_LABEL_IDS,
    projectIds: configs.GITHUB_TODO_PROJECT_IDS,
    milestoneId: todoMilestone.id,
  }
  const doneAndTodoQuest = postIssueToGithub(input)
  log(doneAndTodoQuest)

  const slackBody = body + '  - ' + doneAndTodoQuest.data.createIssue.issue.url
  const doneAndTodoMessage = postMessageToSlack(slackBody)
  log(doneAndTodoMessage)
}

/**
 * スラックにハングアウトのアドレスを投稿する
 */
function postHangoutLinkToSlack() {
  const slackBody = '本日のハングアウト\n' + 'https://hangouts.google.com/group/Air5DyWJnCXGQcsR7'
  const message = postMessageToSlack(slackBody)
  log(message)
}

/**
 * GitHubに、Milestoneを登録する
 * 2019-10-23時点で、GitHub API V4に存在しないので、V3で代替
 *
 * @see https://developer.github.com/v3/issues/milestones/
 */
function postMilestoneToGithub() {
  const configs = getConfigs()
  const method = 'post'
  const path = 'repos/${GITHUB_OWNER}/${GITHUB_REPOSITORY}/milestones'
    .replace('${GITHUB_OWNER}', configs.GITHUB_OWNER)
    .replace('${GITHUB_REPOSITORY}', configs.GITHUB_REPOSITORY)

  const startDate = new Date()
  startDate.setDate(startDate.getDate() + 7)
  const startDateFormat = getDate8(startDate)
  const endDate = new Date()
  endDate.setDate(endDate.getDate() + 14)
  endDate.setHours(23, 59, 59)
  const endDateFormat = getDate8(endDate)

  const payload = {
    title: startDateFormat + '-' + endDateFormat,
    due_on: endDate.toISOString(), // ex: 2019-11-06T15:16:00.908Z, GitHub仕様では、末尾のミリ秒は含まないが、実行可能
  }
  const json = postGithubV3(method, path, payload)
  log(json)
}

/**
 * 木曜にその日までのをMilestoneクローズし、
 * オープンなイシューは次週のマイルストーンへ移行する
 */
function updateIssueAndCloseMilestone() {
  const configs = getConfigs()
  const milestones = fetchMilestonesFromGithub(['OPEN'], 'ASC', 2)
  const currentMilestone = milestones.data.repository.milestones.edges[0].node
  const nextMilestone = milestones.data.repository.milestones.edges[1].node
  const currentMilestoneNum = currentMilestone.number
  const currentMilestoneId = currentMilestone.id
  const nextMilestoneId = nextMilestone.id
  const currentMilestoneUrl = currentMilestone.url
  const nextMilestoneUrl = nextMilestone.url
  log(milestones)

  const fetchIssue = fetchOpenIssueFromMilestone(currentMilestoneNum)
  const targetIssue = fetchIssue.data.repository.milestone.issues.edges

  log(targetIssue)

  for(var i in targetIssue) {
        var issueId = fetchIssue.data.repository.milestone.issues.edges[i].node.id
        var input = {
          id: issueId,
          milestoneId: nextMilestoneId,
        }
        var updateIssue = updateIssueInGithub(input)
        log(updateIssue)
  }

  const closeMilestone = closeMilestoneInGithub(currentMilestoneNum)

  const slackBody = '\
- 今週のMilestoneをクローズしました\n\
  - ${currentMilestoneUrl}\n\
- OPENなクエストは、次週のMilestoneへ移行しました\n\
- 出来そうなクエストがあればチャレンジしてみましょう！Let\'s try it!\n\
  - ${nextMilestoneUrl}\n\
- 以下から自動送信\n\
  - ${configs.URL_GAS}\n\
  - ${configs.URL_GAS_SOURCE}\n\
'
    .replace('${currentMilestoneUrl}', currentMilestoneUrl)
    .replace('${nextMilestoneUrl}', nextMilestoneUrl)
    .replace('${configs.URL_GAS}', configs.URL_GAS)
    .replace('${configs.URL_GAS_SOURCE}', configs.URL_GAS_SOURCE)
  log(slackBody)

  const message = postMessageToSlack(slackBody)
  log(message)
}

/**
 * GitHubから、リポジトリ関連情報を取得する
 *
 * @see https://developer.github.com/v4/object/repository/
 */
function fetchRepositoryInfoFromGithub() {
  const configs = getConfigs()
  const query = 'query($owner: String!, $name: String!) {\
    repository(owner: $owner, name: $name) {\
      id\
      assignableUsers(first: 100) { edges { node { id, url, login, name } } }\
      labels(first: 100) { edges { node { id, url, name } } }\
      projects(first: 100) { edges { node { id, url, name } } }\
      milestones(first: 100) { edges { node { id, url, title } } }\
    }\
  }'
  const variables = {
    owner: configs.GITHUB_OWNER,
    name: configs.GITHUB_REPOSITORY,
  }
  const json = postGithubV4(query, variables)
  log(json)
}


// ## library

/**
 * GitHubから、Milestoneを取得する
 *
 * @see https://developer.github.com/v4/object/repository/#milestones
 *
 * @param {string[]} OPEN/CLOSED
 * @param {string} ASC: 昇順/DESC: 降順
 * @param {int} 先頭からの参照数
 * @return {Object} レスポンスJSON
 */
function fetchMilestonesFromGithub(states, direction, first) {
  const configs = getConfigs()
  const query = 'query($owner: String!, $name: String!, $states: [MilestoneState!], $direction: OrderDirection!, $first: Int) {\
    repository(owner: $owner, name: $name) {\
      milestones(first: $first, states: $states, orderBy: { field: DUE_DATE, direction: $direction }) {\
        edges { node { id, url, number, title } }\
      }\
    }\
  }'
  const variables = {
    owner: configs.GITHUB_OWNER,
    name: configs.GITHUB_REPOSITORY,
    states: states,
    direction: direction,
    first: first,
  }
  const json = postGithubV4(query, variables)
  // NOTE ひとまず、必ず返却される想定

  return json
}

/**
 * MilestoneからOPENなIssueを取得する
 *
 * @see https://developer.github.com/v4/object/issueconnection/
 *
 * @param {Int} Milestoneナンバー
 * @return {Object} レスポンスJSON
 */
function fetchOpenIssueFromMilestone(number) {
  const configs = getConfigs()
  const query = 'query($owner: String!, $name: String!, $number: Int!) {\
    repository(owner: $owner, name: $name) {\
      milestone(number: $number) {\
        issues(first: 100, states: OPEN) {\
          edges { node { id } }\
        }\
      }\
    }\
  }'
  const variables = {
    owner: configs.GITHUB_OWNER,
    name: configs.GITHUB_REPOSITORY,
    number: number,
  }
  const json = postGithubV4(query, variables)

  return json
}

/**
 * 対象のIssueを次の期間のMilestoneへ移動させる
 *
 * @see https://developer.github.com/v4/mutation/updateissue/
 *
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function updateIssueInGithub(input) {
  const configs = getConfigs()
  const query = 'mutation($input: UpdateIssueInput!) {\
    updateIssue(input: $input) {\
      issue{\
        title,\
        state\
      }\
    }\
  }'
  const variables = {
    input: input,
  }
  const json = postGithubV4(query, variables)

  return json
}

/**
 * 対象のMilestoneをクローズする
 *
 * @see https://developer.github.com/v3/issues/milestones/
 *
 * @param {Int} Milestoneナンバー
 */
function closeMilestoneInGithub(milestoneNum) {
  const configs = getConfigs()
  const method = 'patch'

  const path = 'repos/${GITHUB_OWNER}/${GITHUB_REPOSITORY}/milestones/${targetMilestoneNum}'
    .replace('${GITHUB_OWNER}', configs.GITHUB_OWNER)
    .replace('${GITHUB_REPOSITORY}', configs.GITHUB_REPOSITORY)
    .replace('${targetMilestoneNum}', milestoneNum)

  const payload = {
    state: 'closed',
  }
  const json = postGithubV3(method, path, payload)
  log(json)
}

/**
 * GitHubに、Issueを登録する
 *
 * @see https://developer.github.com/v4/mutation/createissue/
 *
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function postIssueToGithub(input) {
  const query = 'mutation($input: CreateIssueInput!) {\
    createIssue(input: $input) {\
      issue {\
        id\
        url\
        title\
      }\
    }\
  }'
  const variables = {
    input: input,
  }
  const json = postGithubV4(query, variables)

  return json
}

/**
 * Slackに、メッセージを投稿する
 *
 * @see https://api.slack.com/methods/chat.postMessage
 *
 * @param {string} メッセージ
 * @return {Object} レスポンスJSON
 */
function postMessageToSlack(body) {
  const configs = getConfigs()
  const method = 'post'
  const path = 'chat.postMessage' // 'auth.test' // 'api.test'
  const payload = {
    channel: configs.SLACK_CHANNEL,
    text: body,
  }
  const json = postSlack(method, path, payload)

  return json
}

/**
 * GitHub API V4 (GraphQL) に、リクエストする
 *
 * @param {string} GraphQLクエリ
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function postGithubV4(query, variables) {
  const url = 'https://api.github.com/graphql'
  const headers = getOauthJsonHeaders('GITHUB_TOKEN', {
    Accept: 'application/vnd.github.v4.idl',
  })
  const payload = {
    query: query,
    variables: variables,
  }
  const options = {
     method: 'post',
     headers: headers,
     payload: JSON.stringify(payload),
     // muteHttpExceptions: true, // TODO 要調査
  }
  const json = postJson(url, options)

  return json
}

/**
 * GitHub API V3 (REST) に、リクエストする
 *
 * @param {string} HTTPメソッド
 * @param {string} パス
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function postGithubV3(method, path, payload) {
  const url = 'https://api.github.com/' + path
  const headers = getOauthJsonHeaders('GITHUB_TOKEN')
  const options = {
     method: method,
     headers: headers,
     payload: JSON.stringify(payload),
     // muteHttpExceptions: true, // TODO 要調査
  }
  const json = postJson(url, options)

  return json
}

/**
 * Slack API (REST) に、リクエストする
 *
 * @param {string} HTTPメソッド
 * @param {string} パス
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function postSlack(method, path, payload) {
  const url = 'https://slack.com/api/' + path
  const headers = getOauthJsonHeaders('SLACK_TOKEN')
  const options = {
     method: method,
     headers: headers,
     payload: JSON.stringify(payload),
     // muteHttpExceptions: true, // TODO 要調査
  }
  const json = postJson(url, options)

  return json
}

/**
 * JSON APIへの、リクエストヘッダを取得する
 *
 * @param {Object} 追加ヘッダ
 * @return {Object} リクエストヘッダ
 */
function getOauthJsonHeaders(tokenKey, additionalHeaders) {
  const configs = getConfigs()
  const token = configs[tokenKey]
  const headers = {
    // ES2015なら、以下で代替可能
    // Authorization: `Bearer ${token}`,
    Authorization: 'Bearer ${token}'
      .replace('${token}', token),
    'Content-Type': 'application/json; charset=UTF-8',
  }
  if (additionalHeaders) {
    // ES2015なら、以下で代替可能
    // Object.assign(headers, additionalHeaders)
    for (var key in additionalHeaders) headers[key] = additionalHeaders[key]
  }

  return headers
}

/**
 * リクエストする
 *
 * @param {string} リクエスト先URL
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function postJson(url, options) {
  // NOTE url, optionsのログを出力してもよい　ただし、Authorizationは隠蔽する
  const response = UrlFetchApp.fetch(url, options)
  const contentText = response.getContentText()
  const json = JSON.parse(contentText)

  return json
}

/**
 * YYYYMMDD形式の、文字列を取得する
 *
 * @param {Date} 日付
 * @return {string} YYYYMMDD
 */
function getDate8(date) {
  const configs = getConfigs()
  // ex: date.toISOString(): 2019-11-06T15:16:00.908Z => 20191106
  // toISOString()はUTCになるので、locale表示のために、一時的に時差を加算して元に戻す
  date.setHours(date.getHours() + configs.LOCALE_HOUR)
  const date8 = date.toISOString().replace(/-/g, '').replace(/T.*/, '')
  date.setHours(date.getHours() - configs.LOCALE_HOUR)

  return date8
}

/**
 * ログを出力する
 *
 * @param {Object} 出力内容
 */
function log(messageItem) {
  const message = JSON.stringify(messageItem, null, 2)
  // エディタ上で、Cmd + Enter でのログ確認
  Logger.log(message)
  // https://script.google.com/home/executions でのログ確認
  console.log(message)
}

/**
 * 設定情報を取得する
 * function外にconst定義すると再宣言エラーが発生するため、関数化
 */
function getConfigs() {
  const configs = {
    LOCALE_HOUR: +9, // Asia/Tokyo

    TARGET_DAY: 6, // 曜日Sunday - Saturday : 0 - 6
    TARGET_WEEKS: [2, 4],

    URL_MAIN: 'https://github.com/itomakiweb-corp/bank#flow',
    URL_QUEST_LIST: 'https://github.com/itomakiweb-corp/bank/projects/1',
    URL_QUEST_READY: 'https://github.com/itomakiweb-corp/bank/milestone/7',
    URL_QUEST_NEW: 'https://itomakiweb.com/bank/newQuest',
    URL_GAS: 'https://drive.google.com/open?id=15wOLmTL8HGkWKhiFjLSvUCQJPrq2jeAo-4JRuRN_R96NJSUI2UMGIB_v',
    URL_GAS_SOURCE: 'https://github.com/itomakiweb-corp/bank/blob/master/tools/gas-main.js',

    // 設定値は、fetchRepositoryInfoFromGithubの実行などで確認可能
    GITHUB_OWNER: 'itomakiweb-corp',
    GITHUB_REPOSITORY: 'bank',
    GITHUB_REPOSITORY_ID: 'MDEwOlJlcG9zaXRvcnkyMTQxODM2ODE=',
    GITHUB_TODO_ASSIGNEE_IDS: [
      'MDQ6VXNlcjQzMjU1ODgw', // adachi-swivel
      'MDQ6VXNlcjQzNDM1OTg1', // bac0907
      'MDQ6VXNlcjU5NDkzNDgy', // hidecharo
      // 'MDQ6VXNlcjE2NTA1Mjcx', // itomakiweb
      'MDQ6VXNlcjQzMTE0NDQx', // kazucharo
      'MDQ6VXNlcjQ1MDA2Njgz', // tanukinoyu
      'MDQ6VXNlcjQ0MjUyMzIx', // undine411
    ],
    GITHUB_TODO_LABEL_IDS: [
      'MDU6TGFiZWwxNjE1ODYwNTc0', // -priority: 5
      'MDU6TGFiZWwxNjE1ODY0MDA4', // cost-pre: 3
    ],
    GITHUB_TODO_PROJECT_IDS: [
      'MDc6UHJvamVjdDMzNTEwODU=', // Quest
    ],

    // URLでID確認
    // SLACK_CHANNEL: '#tmp', // tmp
    // SLACK_CHANNEL: 'GPDVCDL2F', // tmp
    // SLACK_CHANNEL: 'DCX7CRT6E', // hide
    SLACK_CHANNEL: 'GEED5096Z', // dev-study
  }

  // ファイル => プロジェクトのプロパティ => スクリプトのプロパティ　で設定した環境変数を参照
  // GITHUB_TOKEN, SLACK_TOKENなど
  const scriptProperties = PropertiesService.getScriptProperties().getProperties()
  for (var key in scriptProperties) configs[key] = scriptProperties[key]

  return configs
}


// ## testCode

/**
 * TODO
 */
function test() {
}
