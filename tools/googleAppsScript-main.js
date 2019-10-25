// https://drive.google.com/open?id=15wOLmTL8HGkWKhiFjLSvUCQJPrq2jeAo-4JRuRN_R96NJSUI2UMGIB_v


// ## main（triggersなどからの実行を想定）

/**
 * GitHubに、先週のまとめと今週の予定を確認するクエストを登録する
 * Slackに、先週のまとめと今週の予定を送信する
 */
function postDoneAndTodoToSlack() {
  const dueDate = new Date()
  const dueDateFormat = getDate8(dueDate)
  const title = '先週のまとめと今週と来週の予定を確認して検討する（${dueDateFormat}）'
    .replace('${dueDateFormat}', dueDateFormat)
  log(title)

  const doneMilestone = fetchMilestoneFromGithub(['CLOSED'], 'DESC')
  const todoMilestone = fetchMilestoneFromGithub(['OPEN'], 'ASC')
  log(doneMilestone)
  log(todoMilestone)

  const doneUrl = doneMilestone.url + '?closed=1'
  const todoUrl = todoMilestone.url
  const body = '\
- 先週やった内容\n\
  - ${doneUrl}\n\
- 今週やる予定\n\
  - ${todoUrl}\n\
- 以下から自動送信\n\
  - https://drive.google.com/open?id=15wOLmTL8HGkWKhiFjLSvUCQJPrq2jeAo-4JRuRN_R96NJSUI2UMGIB_v\
'
    .replace('${doneUrl}', doneUrl)
    .replace('${todoUrl}', todoUrl)
  log(body)

  const configs = getConfigs()
  const variables = {
    repositoryId: configs.GITHUB_REPOSITORY_ID,
    title: title,
    body: body,
    // TODO assigneeIds: configs.GITHUB_TODO_ASSIGNEE_IDS,
    labelIds: configs.GITHUB_TODO_LABEL_IDS,
    projectIds: configs.GITHUB_TODO_PROJECT_IDS,
    milestoneId: todoMilestone.id,
  }
  const doneAndTodoQuest = postIssueToGithub(variables)
  log(doneAndTodoQuest)

  const doneAndTodoMessage = postMessageToSlack(body)
  log(doneAndTodoMessage)
}

/**
 * GitHubに、Milestoneを登録する
 * 2019-10-23時点で、GitHub GraphQL APIに存在しないので、v3で代替
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
    title: 'sprint: ' + startDateFormat + '-' + endDateFormat,
    due_on: endDate.toISOString(), // ex: 2019-11-06T15:16:00.908Z, GitHub仕様では、末尾のミリ秒は含まないが、実行可能
  }
  const json = postGithubV3(method, path, payload)
  log(json)
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
 * @return {Object} Milestone
 */
function fetchMilestoneFromGithub(states, direction) {
  const configs = getConfigs()
  const query = 'query($owner: String!, $name: String!, $states: [MilestoneState!], $direction: OrderDirection!) {\
    repository(owner: $owner, name: $name) {\
      milestones(first:1, states: $states, orderBy: { field: DUE_DATE, direction: $direction }) {\
        edges { node { id, url, title } }\
      }\
    }\
  }'
  const variables = {
    owner: configs.GITHUB_OWNER,
    name: configs.GITHUB_REPOSITORY,
    states: states,
    direction: direction,
  }
  const json = postGithubV4(query, variables)
  // NOTE ひとまず、必ず返却される想定
  const milestone = json.data.repository.milestones.edges[0].node

  return milestone
}

/**
 * GitHubに、Issueを登録する
 *
 * @see https://developer.github.com/v4/mutation/createissue/
 *
 * @param {Object} リクエスト内容
 * @return {Object} レスポンスJSON
 */
function postIssueToGitHub(variables) {
  const query = 'mutation($input: CreateIssueInput!) {\
    createIssue(input:$input) {\
      issue {\
        title,\
        url\
      }\
    }\
  }';
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

    GITHUB_OWNER: 'itomakiweb-corp',
    GITHUB_REPOSITORY: 'bank',
    // 以下コマンドでID確認
    // https://brew.sh/index_ja
    // brew install jq
    // token="正当な値を設定"
    // curl -s -X POST -H "Authorization: Bearer ${token}" -H 'Accept: application/vnd.github.v4.idl' -d '{"query": "query { repository(owner:\"itomakiweb-corp\", name:\"bank\") { id, assignableUsers(first: 100) { edges { node { id, login, name } } }, labels(first: 100) { edges { node { id, name } } }, projects(first: 100) { edges { node { id, name } } }, milestones(first: 100) { edges { node { id, title } } } } }"' https://api.github.com/graphql | jq
    GITHUB_REPOSITORY_ID: 'MDEwOlJlcG9zaXRvcnkyMTQxODM2ODE=',
    GITHUB_TODO_ASSIGNEE_IDS: [
      'MDQ6VXNlcjQzMjU1ODgw', // adachi-swivel
      'MDQ6VXNlcjQzNDM1OTg1', // bac0907
      'MDQ6VXNlcjE2NTA1Mjcx', // itomakiweb
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
    SLACK_CHANNEL: 'GPDVCDL2F', // tmp
    // SLACK_CHANNEL: 'DCX7CRT6E', // hide
    // SLACK_CHANNEL: 'GEED5096Z', // dev-study
  }

  return configs
}

function tmp() {
  const dueDate = new Date()
  dueDate.setHours(8)
  const dueDateFormat = getDate8(dueDate)
  const title = '先週のまとめと今週と来週の予定を確認して検討する（${dueDateFormat}）'
    .replace('${dueDateFormat}', dueDateFormat)
  log(title)
}

// TODO testコードを追加

/*
## memo

- GASエディタ操作
  - Cmd + r: 実行
  - Cmd + Enter: ログ確認
- GAS環境変数
  - ファイル => プロジェクトのプロパティ => スクリプトのプロパティ　で設定可能
  - PropertiesService.getScriptProperties().getProperty(keyName)で参照
- GAS定期実行
  - https://script.google.com/home/triggers
  - https://script.google.com/home/executions
- GitHub token
  - https://github.com/settings/tokens
  - https://developer.github.com/apps/building-oauth-apps/understanding-scopes-for-oauth-apps/
- GraphQL
  - variablesを使う場合、query()内にも定義が必要なので注意
  - https://developer.github.com/v4/explorer/
    - query詳細を入力しながら確認
  - https://employment.en-japan.com/engineerhub/entry/2018/12/26/103000
    - 概要確認
  - https://graphql.github.io/learn/queries/
    - 未読
- Slack token
  - https://api.slack.com/apps
  - https://api.slack.com/apps/APQQU1DQU/oauth?
  - not_authedエラー
    - BearerのBを小文字にしていたら、Slack APIにアクセスできなかった
    - GitHub APIはアクセス可能だったので、調査が難航した
    - 以下サンプルが小文字になっていたため、間違えた
    - https://developer.github.com/v4/guides/forming-calls/#communicating-with-graphql
  - channel_not_foundエラー
    - private channelは、botの招待が必要
    - サイドメニュー => App + => itomakiweb-botを表示 => @itomakiweb-botをクリック => チャンネルにこのアプリを連携
    - https://app.slack.com/client/TCWKJAQG1/DPTFFNKJA
    - 
*/
