## Flow

1. [クエスト](https://github.com/itomakiweb-corp/bank/projects/1)を確認する（[クエストを追加する](https://itomakiweb.com/bank/newQuest)）
    - 優先度が高く、着手可能なクエストが、上に並んでいる
    - 列の種別: memo, dev, required, priority5, priority3, priority1, optional
    - labelのpriority: 自分が思う優先度を暫定で設定する
    - labelのcost-pre: [ランダムで、人を選ぶ機能を開発する](https://github.com/itomakiweb-corp/bank/issues/4)の完了を1とした場合の相対値を暫定で設定する
    - 自分ができないタスクでも、クエストに登録するのを推奨する
        - 誰かが分解できるから
        - 分解したら、元のクエストは分解クエストと名称を変更してクローズする
    - 重要なクエストは、人数分作成する（場合によっては、全員を1つのクエストにassign）
    - 取れるクエストは、随時自分をassignして問題ない（学習目的なので、積極的にクエストを担当するのを推奨）
    - やってみよう！　の精神を大事に
1. [先週のまとめ](https://github.com/itomakiweb-corp/bank/milestones?state=closed)と[今週と来週の予定](https://github.com/itomakiweb-corp/bank/milestones)を確認して検討する
    - 今回対応するクエストを、全員で決定する
    - 来週対応するクエストも、同時に検討する
1. [モブプログラミングの担当をランダムに決める](https://paiza.io/projects/SHMoTiDcBPG9eI86P-WS5A)（[ソースコード](https://github.com/itomakiweb-corp/bank/blob/master/tools/selectRandomUsers.kt)）
    - 30分交代でプルリクエスト作成まで完了させる（途中でも問題ない）
1. web 対応するクエストを開き、自分をassignする
1. git checkout master
1. git pull # origin master
1. git branch -a
1. git branch -d new-branch # 必要に応じて削除を実行する（マージ前なら、-D指定）
1. git checkout -b new-branch
1. 新しいブランチ上で必要な修正を実施する
1. emulatorなどで動作確認する
1. git add .
1. git commit -av
1. git push origin new-branch -u -n
1. web [pull request](https://github.com/itomakiweb-corp/bank/pulls), reviewersを指定する
1. web reviewersのレビュー/マージ/new-branch削除を待つ
1. 終わったクエストに、cost-realを設定する
1. 最初に戻る
1. その週のクエストが全て終わったら、Milestoneをクローズする
    - 終わらなかったクエストを、unfinishedとしてMilestoneの説明文に追記する
    - 終わらなかったクエストを、翌週のMilestoneに移動する

## Rule

|対象|命名規則|備考|
|:---|:---|:---|
|git branch name|lower-kebab-case|開発している機能名などを推奨する|
|kotlin class name|UpperCamelCase|PascalCaseとも呼ばれる|
|kotlin fun name|lowerCamelCase|-|
|kotlin const name|UPPER_SNAKE_CASE|定数|
|View ID|lowerCamelCase|Kotlin変数名として記述することになるので、kebab-caseは不可|
|Resource Name|lowerCamelCase|デフォルトはlower_snake_caseの模様だが、View IDと合わせる|

## Screen

|Activity|説明|備考|
|:---|:---|:---|
|MainActivity|ゲーム一覧|-|
|BlackJackActivity|ブラックジャック詳細|-|
|HighAndLowActivity|ハイアンドロー詳細|-|
|QuestNewActivity|クエスト発行|-|

## Class

|クラス|説明|備考|
|:---|:---|:---|
|DeckOfCards|トランプ一覧|-|
|TODO|TODO|-|

## Env

- install
    - https://git-scm.com/downloads
    - https://developer.android.com/studio?hl=ja
- open -a 'Android Studio'
    - Check out project from Version Control, Git
    - https://github.com/itomakiweb-corp/bank.git
- set ProjectRoot/local.properties (inside Gradle Scripts)

    ```
    GITHUB_TOKEN={YOUR_TOKEN_HERE}
    SLACK_TOKEN={YOUR_TOKEN_HERE}
    ```
    - 波括弧｛｝は不要です。

- Run 'app'
    - Open AVD Manager
    - Create Virtual Device
    - Pixel 3, Q (Android 10.0)
- Other for Mac
    - https://brew.sh/index_ja
- bots
    - https://drive.google.com/open?id=15wOLmTL8HGkWKhiFjLSvUCQJPrq2jeAo-4JRuRN_R96NJSUI2UMGIB_v
        - 閲覧権限でも実行できてしまうので注意（環境変数は見られない）
    - https://script.google.com/home/executions
- links
  - https://android.benigumo.com/20190219/spacex-rest-api-retrofit-coroutine/
  - https://square.github.io/retrofit/
  - https://kotlinlang.org/docs/reference/coroutines/basics.html
  - https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#structured-concurrency-lifecycle-and-coroutine-parent-child-hierarchy
  
## etc

- [memo.md](docs/memo.md)
- 2019-10-17時点で、MacのAndroid Studio 3.5.1で、以下warningが出る
    - 動作には問題ない模様なので、無視しても良い
    - ProjectRoot/.ideaディレクトリを削除すればローカルを正常化できるが、全体に反映する方法はない模様

    ```
    Unsupported Modules Detected: Compilation is not supported for following modules: bank. Unfortunately you can't have non-Gradle Java modules and Android-Gradle modules in one project.
    ```

- 2019-10-22時点で、MacのAndroid Studio 3.5.1で、emulator実行時に、以下warningが出る
    - 動作には問題ない模様なので、無視しても良い
    - 正常化する方法はわからない

    ```
    Qt WebEngine ICU data not found at /Users/joshuaduong/qt-build-5.12.1/install-darwin-x86_64/resources. Trying parent directory...
    ...
    ```

- Screen備考に、画面遷移を記述するか？
    - 画像で、画面遷移を作成するか？
        - モブプログラミングのメンバで、どうするかを決定して作成していく
