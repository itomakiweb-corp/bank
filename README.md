## Flow

1. git checkout master
1. git pull origin master
1. git branches, new branch
1. 新しいブランチ上で必要な修正を実施
1. emulatorなどで動作確認
1. git commit
1. git push
1. web pull request, reviewersを指定
1. web reviewersのレビューとマージ待ち
1. 最初に戻る

## Rule

|項目|内容|備考|
|:---|:---|:---|
|ブランチ名|kebab-case|-|
|クラス名|UpperCamelCase|-|
|View ID|lowerCamelCase|Kotlin変数名として記述することになるので、kebab-caseは不可|
|Resource Name|lowerCamelCase|-|

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

## etc

- [memo.md](documents/memo.md)

<!---
# memo

- test
  - test
    - test
-->
