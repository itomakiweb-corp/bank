## Flow

1. [クエスト](https://github.com/itomakiweb-corp/bank/projects/1?fullscreen=true)を確認する
1. [先週のまとめ](https://github.com/itomakiweb-corp/bank/milestones?state=closed)と[今週と来週の予定](https://github.com/itomakiweb-corp/bank/milestones)を確認して検討する
1. [モブプログラミングの担当をランダムに決める](https://paiza.io/projects/SHMoTiDcBPG9eI86P-WS5A)（[ソースコード](https://github.com/itomakiweb-corp/bank/blob/master/tools/selectRandomUsers.kt)）
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
|ブランチ名|kebab-case|開発している機能名などを推奨する|
|クラス名|UpperCamelCase|-|
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
  
## etc

- [memo.md](documents/memo.md)
- 2019-10-17時点で、MacのAndroid Studio 3.5.1で、以下warningが出る
  - 動作には問題ない模様なので、無視しても良い
  - ProjectRoot/.ideaディレクトリを削除すればローカルを正常化できるが、全体に反映する方法はない模様
```
Unsupported Modules Detected: Compilation is not supported for following modules: bank. Unfortunately you can't have non-Gradle Java modules and Android-Gradle modules in one project.
```
- Screen備考に、画面遷移を記述するか？
  - 画像で、画面遷移を作成するか？
    - モブプログラミングのメンバで、どうするかを決定して作成していく
