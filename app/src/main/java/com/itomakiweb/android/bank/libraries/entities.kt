package com.itomakiweb.android.bank.libraries

import android.content.Context
import android.widget.LinearLayout
import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable
import kotlin.random.Random

class Ref {
    companion object {
        const val TAG_AUTH = "AnonymousAuth"
        const val TAG_FIRESTORE = "Firestore"
        const val TAG_DEBUG = "DebugLog"

        const val match = LinearLayout.LayoutParams.MATCH_PARENT
        const val wrap = LinearLayout.LayoutParams.WRAP_CONTENT

        fun getBet(countGame: Long, moneyRate: Long): Long {
            if (countGame >= 10) { // TODO confirm
                return moneyRate
            }

            return (countGame + 1) * moneyRate
        }
    }
}

@JsonClass(generateAdapter = true)
data class GithubGraphqlInput(
    val query: String,

    val variables: Any
)

@JsonClass(generateAdapter = true)
data class GithubGraphqlOutput(
    val data: GithubQuery,

    // TODO どうすべきか検討
    val errors: Any?
    // TOOD error val errors: Map<String?, String?>?
): Serializable

@JsonClass(generateAdapter = true)
data class GithubQuery(
    val repository: GithubRepository
)

@JsonClass(generateAdapter = true)
data class GithubRepository(
    val milestones: GithubMilestoneConnection,

    val issues: GithubIssueConnection?
)

@JsonClass(generateAdapter = true)
data class GithubMilestoneConnection(
    val nodes: List<GithubMilestone>
)

@JsonClass(generateAdapter = true)
data class GithubMilestone(
    val id: String,

    val url: String,

    val title: String,

    val issues: GithubIssueConnection
)

@JsonClass(generateAdapter = true)
data class GithubIssueConnection(
    val nodes: List<GithubIssue>
)

@JsonClass(generateAdapter = true)
data class GithubCreateIssueInput(
    val title: String,

    val body: String,

    val assigneeIds: List<String>,

    val labelIds: List<String>,

    val projectIds: List<String> = BuildConfig.GITHUB_TODO_PROJECT_IDS.toList(),

    val milestoneId: String = BuildConfig.GITHUB_MILESTONE_ID,

    val repositoryId: String = BuildConfig.GITHUB_REPOSITORY_ID
)

@JsonClass(generateAdapter = true)
data class GithubUpdateIssueInput(
    val id: String,

    val state: String,

    val title: String,

    val body: String,

    val assigneeIds: List<String>,

    val labelIds: List<String>,

    val projectIds: List<String> = BuildConfig.GITHUB_TODO_PROJECT_IDS.toList(),

    val milestoneId: String = BuildConfig.GITHUB_MILESTONE_ID,

    val repositoryId: String = BuildConfig.GITHUB_REPOSITORY_ID
)

@JsonClass(generateAdapter = true)
data class GithubCreateIssueOutput(
    val data: GithubCreateIssue,

    // TODO どうすべきか検討
    val errors: Any?
)

@JsonClass(generateAdapter = true)
data class GithubCreateIssue(
    val createIssue: GithubIssueParent
)

@JsonClass(generateAdapter = true)
data class GithubIssueParent(
    val issue: GithubIssue
)

@JsonClass(generateAdapter = true)
data class GithubIssue(
    val id: String,

    val url: String,

    val title: String
) {
    // TODO 簡易実装版なので、削除するかも
    override fun toString(): String {
        return title
    }
}

@JsonClass(generateAdapter = true)
data class GithubUser(
    @Json(name = "node_id")
    val nodeId: String,

    val id: String,

    val url: String,

    val login: String,

    val name: String,

    @Json(name = "avatar_url")
    val avatarUrl: String
)

@JsonClass(generateAdapter = true)
data class GithubRepositoriesOutput(
    @Json(name = "total_count")
    val totalCount: Int
)


@JsonClass(generateAdapter = true)
data class SlackChatMessageInput(
    val text: String,

    val channel: String = BuildConfig.SLACK_CHANNEL
)

@JsonClass(generateAdapter = true)
data class SlackChatMessageOutput(
    val ok: Boolean
)


fun startHighAndLow() {
    val deckOfCards = DeckOfCards()
    val you = You()
    val highAndLow = HighAndLow(deckOfCards, you)
    highAndLow.start()
}

class HighAndLow(val deckOfCards: DeckOfCards, val you: You) {
    companion object {
        val MIDDLE_RANK = 7
        val INIT_MONEY = 10000
        val BASE_MONEY = 1000
        val CLEAR_MONEY = 20000
        val MAX_BET = 10
    }

    val usedCards = mutableListOf<Card>()
    var betCount = 0
    var gameCount = 0
    var winCount = 0
    var winStreakCount = 0
    var maxWinStreakCount = 0

    fun start() {
        begin()
        while (true) {
            // card size
            if (deckOfCards.size() < 1) {
                reset(false)
                continue
            }

            // money
            println()
            val betMoney = (++betCount % MAX_BET) * BASE_MONEY
            println("${you.name} money is ${you.money}")

            // game clear
            if (you.money > CLEAR_MONEY) {
                println("Game Clear")
                break
            }

            // game over
            if (you.money < BASE_MONEY) {
                println("Game Over")
                break
            }

            // call
            val yourCall = you.call(betMoney)
            println("Bet ${betMoney}")
            println("${you.name} call ${yourCall}")

            // drop out
            if (yourCall == HighAndLowCall.DROP_OUT) {
                reset(true)
                continue
            }

            // game
            gameCount++
            val card = deckOfCards.draw()
            val result = getResult(card)
            usedCards.add(card)
            println("Card is ${card}")
            if (yourCall == result) {
                println("${you.name} Win")
                you.addMoney(betMoney)
                winCount++
                winStreakCount++
                if (winStreakCount > maxWinStreakCount) {
                    maxWinStreakCount = winStreakCount
                }
            } else {
                println("${you.name} Lose")
                you.betMoney(betMoney)
                winStreakCount = 0
            }
        }
        showRecord()
        showCards(deckOfCards.cards)
    }

    fun begin() {
        println("High And Low start")
        deckOfCards.removeCards(Rank.SEVEN)
        deckOfCards.shuffle()
    }

    fun reset(isResetWinStreakCount: Boolean = false) {
        deckOfCards.addCards(usedCards)
        usedCards.removeAll { true }
        deckOfCards.shuffle()
        betCount = 0
        if (isResetWinStreakCount) winStreakCount = 0
    }

    fun getResult(card: Card): HighAndLowCall {
        if (card.rank.number > MIDDLE_RANK) return HighAndLowCall.HIGH
        else return HighAndLowCall.LOW
    }

    fun showRecord() {
        println()
        println("gameCount: ${gameCount}")
        println("winCount: ${winCount}")
        println("winRate: ${100 * winCount / gameCount}%")
        println("winStreakCount: ${winStreakCount}")
        println("maxWinStreakCount: ${maxWinStreakCount}")
    }

    fun showCards(cards: List<Card>) {
        println()
        println("Remaining: ${cards.size} card")
        cards.chunked(10).forEach { println(it.joinToString(",")) }
        // cards.chunked(10).forEach { println(it) }
        // for (cardChunks in cards.chunked(10)) println(cardChunks)
        // for (cardChunks in cards.chunked(Rank.values().size)) println(cardChunks)
        // println(cards)
        /*
        for (card in cards) {
            println(card)
        }
        */
    }
}

class You : Player("You", HighAndLow.INIT_MONEY) {
    override fun call(betMoney: Int): HighAndLowCall {
        if (money < betMoney) return HighAndLowCall.DROP_OUT

        val randomInt = Random.nextInt(0, 30) // 0..29
        when {
            randomInt < 3 -> return HighAndLowCall.DROP_OUT
            randomInt < 20 -> return HighAndLowCall.HIGH
            else -> return HighAndLowCall.LOW
        }
    }
}

abstract class Player(val name: String, var money: Int) {
    val cards = mutableListOf<Card>()

    fun betMoney(money: Int) {
        this.money -= money
    }

    fun addMoney(money: Int) {
        this.money += money
    }

    fun memoryCard(card: Card) {
        cards.add(card)
    }

    abstract fun call(betMoney: Int): HighAndLowCall
}

enum class ResultGame {
    WIN,
    LOSE,
    // exception
    BEGIN
}

enum class HighAndLowCall {
    HIGH,
    LOW,
    DROP_OUT,
    // exception
    BEGIN
}

data class DeckOfCards(val cards: MutableList<Card> = mutableListOf<Card>()) {


    init {
        for (suit in Suit.values()) {
            if (suit == Suit.BEGIN) continue

            for (rank in Rank.values()) {
                if (rank == Rank.BEGIN) continue

                cards.add(Card(suit, rank))
            }
        }
    }

    fun shuffle() {
        cards.shuffle()
        /*
        cards[0] = Card(Mark.SPADE, 11)
        cards[1] = Card(Mark.SPADE, 13)
        cards[2] = Card(Mark.SPADE, 1)
        cards[0] = Card(Mark.SPADE, 1)
        cards[1] = Card(Mark.SPADE, 1)
        cards[2] = Card(Mark.SPADE, 1)
        cards[3] = Card(Mark.SPADE, 1)
        */
    }

    fun removeCards(targetRank: Rank) {
        cards.removeAll { it.rank == targetRank }
    }

    fun draw(): Card {
        return cards.removeAt(0)
    }

    fun size(): Int {
        return cards.size
    }

    fun addCards(addCards: List<Card>) {
        cards.addAll(addCards)
    }
}

data class Card(
    val suit: Suit,
    val rank: Rank
) {
    override fun toString(): String {
        return "(${suit.symbol}%2s)".format(rank.symbol)
    }

    fun getResourceId(context: Context): Int {
        val drawableName = "card_%s_%02d".format(suit.resId, rank.number)

        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }
}

enum class Suit(val symbol: String, val resId: String) {
    SPADE("♠", "spade"),
    HEART("♡", "heart"),
    DIAMOND("◇", "diamond"),
    CLUB("♣", "club"),
    // exception
    BEGIN("-", "back")
}

enum class Rank(val symbol: String, val number: Int) {
    ACE("A", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    // exception
    BEGIN("-", 0)
}
