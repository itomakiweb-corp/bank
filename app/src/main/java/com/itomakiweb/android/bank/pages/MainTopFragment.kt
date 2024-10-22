package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.itomakiweb.android.bank.BuildConfig

import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.GithubApi
import com.itomakiweb.android.bank.libraries.GithubGraphqlInput
import com.itomakiweb.android.bank.libraries.ScopedFragment
import kotlinx.android.synthetic.main.fragment_main_top.*
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.schedule

/**
 * A simple [Fragment] subclass.
 */
class MainTopFragment: ScopedFragment() {
    private lateinit var timer: TimerTask

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer = Timer().schedule(1800, 4000) {
            topNotice.run {
                // 画面遷移後に、nullとなるので、チェック
                // TODO 他に良いやり方がないか再確認　また全体的にアニメーション設定再確認
                if (this == null) return@run

                postDelayed({
                    animate().alpha(0f).duration = 800
                }, 1200)
                postDelayed({
                    animate().alpha(1f).duration = 1600
                }, 2400)
            }
        }

        //fetchGithubIssues()

    }
/*
    override fun onDestroyView() {
        super.onDestroyView()

        // アニメーションキャンセル
        timer.cancel()

        // アプリ再起動時に、一覧が重なって表示されることがあるので、リストクリア
        questThisWeek.adapter = null
    }


    fun fetchGithubIssues() {
        // TODO いずれ、処理の共通化を検討
        runBlocking {
            try {
                val payload = GithubGraphqlInput(
                    query = """query(${'$'}owner: String!, ${'$'}name: String!) {
    repository(owner: ${'$'}owner, name: ${'$'}name) {
        milestones(first: 1, states: [OPEN], orderBy: { field: DUE_DATE, direction: ASC }) {
            nodes {
                id
                url
                number
                title
                issues(first: 100, states: [OPEN], orderBy: { field: UPDATED_AT, direction: DESC }) {
                    nodes {
                        id
                        url
                        number
                        title
                    }
                }
            }
        }
    }
}
""",
                    variables = mapOf(
                        "owner" to BuildConfig.GITHUB_OWNER,
                        "name" to BuildConfig.GITHUB_REPOSITORY
                    )
                )
                Log.i("api", "${payload}")

                val output = GithubApi.instance.query(payload)
                val resultText = "${output}"
                val questThisWeekItems = output.data.repository.milestones.nodes[0].issues.nodes
                questThisWeek.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, questThisWeekItems)
                questThisWeek.setOnItemClickListener { adapterView, view, i, l ->
                    val questItem = questThisWeekItems[i]
                    val uri = Uri.parse(questItem.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }

                Log.i("api", resultText)
            } catch (e: Exception) {
                val resultText = e.message

                Log.w("api", resultText, e)
            }
        }
    }
    */
}
