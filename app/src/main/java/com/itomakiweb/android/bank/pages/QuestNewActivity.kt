package com.itomakiweb.android.bank.pages

import android.os.Bundle
import android.util.Log
import com.itomakiweb.android.bank.BuildConfig
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.GithubApi
import com.itomakiweb.android.bank.libraries.GithubCreateIssueInput
import com.itomakiweb.android.bank.libraries.GithubGraphqlInput
import com.itomakiweb.android.bank.libraries.ScopedAppActivity
import kotlinx.android.synthetic.main.activity_quest_new.*
import kotlinx.coroutines.runBlocking

class QuestNewActivity : ScopedAppActivity() {
    val priorityMap = mapOf(
        R.id.priority1 to BuildConfig.GITHUB_PRIORITY1,
        R.id.priority3 to BuildConfig.GITHUB_PRIORITY3,
        R.id.priority5 to BuildConfig.GITHUB_PRIORITY5
    )
    val costPreMap = mapOf(
        R.id.costPre0 to BuildConfig.GITHUB_COST_PRE0,
        R.id.costPre1 to BuildConfig.GITHUB_COST_PRE1,
        R.id.costPre2 to BuildConfig.GITHUB_COST_PRE2,
        R.id.costPre3 to BuildConfig.GITHUB_COST_PRE3,
        R.id.costPre5 to BuildConfig.GITHUB_COST_PRE5,
        R.id.costPreX to BuildConfig.GITHUB_COST_PREX
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_new)

        milestoneId.setText(BuildConfig.GITHUB_MILESTONE_ID)
        createQuest.setOnClickListener {
            if (titleQuest.text.isEmpty()) {
                titleQuest.error = "required"
                return@setOnClickListener
            }

            val assigneeIds = if (assignAll.isChecked) {
                BuildConfig.GITHUB_TODO_ASSIGNEE_IDS.toList()
            } else {
                listOf<String>()
            }
            val input = GithubCreateIssueInput(
                title = titleQuest.text.toString(),
                body = body.text.toString(),
                assigneeIds = assigneeIds,
                labelIds = listOf(
                    priorityMap[priority.checkedRadioButtonId] ?: BuildConfig.GITHUB_PRIORITY3,
                    costPreMap[costPre.checkedRadioButtonId] ?: BuildConfig.GITHUB_COST_PRE3
                ),
                milestoneId = milestoneId.text.toString()
            )
            createGithubIssue(input)
        }
    }

    fun createGithubIssue(input: GithubCreateIssueInput) {
        // TODO いずれ、処理の共通化を検討
        runBlocking {
            try {
                val payload = GithubGraphqlInput(
                    query = """mutation(${'$'}input: CreateIssueInput!) {
    createIssue(input: ${'$'}input) {
        issue {
            id
            url
            title
        }
    }
}
""",
                    variables = mapOf(
                        "input" to input
                    )
                )
                Log.i("api", "${payload}")

                val issueOutput = GithubApi.instance.createIssue(payload)
                val resultText = "${issueOutput}"
                result.setText(resultText)

                Log.i("api", resultText)
            } catch (e: Exception) {
                val resultText = e.message
                result.setText(resultText)

                Log.w("api", resultText, e)
            }
        }
    }
}
