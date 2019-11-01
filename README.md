## Flow

1. [クエスト](https://github.com/itomakiweb-corp/bank/projects/1)を確認する（[クエストを追加する](https://itomakiweb.com/bank/newQuest)）
1. [先週のまとめ](https://github.com/itomakiweb-corp/bank/milestones?state=closed)と[今週と来週の予定](https://github.com/itomakiweb-corp/bank/milestones)を確認して検討する
1. [モブプログラミングの担当をランダムに決める](https://paiza.io/projects/SHMoTiDcBPG9eI86P-WS5A)（[ソースコード](https://github.com/itomakiweb-corp/bank/blob/master/tools/selectRandomUsers.kt)）
    - 30分交代でプルリクエスト作成まで完了させる（途中でも問題ない）
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
1. 最初に戻る

## Rule

|対象|命名規則|備考|
|:---|:---|:---|
|git branch name|kebab-case|開発している機能名などを推奨する|
|kotlin class name|UpperCamelCase|-|
|kotlin fun name|lowerCamelCase|-|
|View ID|lowerCamelCase|Kotlin変数名として記述することになるので、kebab-caseは不可|
|Resource Name|lowerCamelCase|デフォルトはsnake_caseの模様だが、View IDと合わせる|

## Screen

|Activity|説明|備考|
|:---|:---|:---|
|MainActivity|ゲーム一覧|-|
|BlackJackActivity|ブラックジャック詳細|-|
|HighAndLowActivity|ハイアンドロー詳細|-|
|test|test|test|

## Class

|クラス|説明|備考|
|:---|:---|:---|
|DeckOfCards|トランプ一覧|-|
|TODO|TODO|-|

## Env

- install
    - https://git-scm.com/downloads
    - https://developer.android.com/studio?hl=ja
- open Android Studio
    - Check out project from Version Control, Git
    - https://github.com/itomakiweb-corp/bank.git
- Run 'app'
    - Open AVD Manager
    - Create Virtual Device
    - Pixel 3, Q (Android 10.0)
- Other for Mac
    - https://brew.sh/index_ja
- bots
    - https://drive.google.com/open?id=15wOLmTL8HGkWKhiFjLSvUCQJPrq2jeAo-4JRuRN_R96NJSUI2UMGIB_v
        - 閲覧権限でも実行できてしまうので注意（環境変数は見られない）
  
## etc

- [memo.md](documents/memo.md)
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
