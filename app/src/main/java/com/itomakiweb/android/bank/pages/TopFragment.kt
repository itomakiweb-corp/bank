package com.itomakiweb.android.bank.pages


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import com.itomakiweb.android.bank.BuildConfig

import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.GithubApi
import com.itomakiweb.android.bank.libraries.GithubGraphqlInput
import com.itomakiweb.android.bank.libraries.ScopedFragment
import kotlinx.android.synthetic.main.fragment_top.*
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 */
class TopFragment: ScopedFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetchGithubIssues()

    }

    fun fetchGithubIssues() {
        // TODO いずれ、処理の共通化を検討
        runBlocking {
            try {
                val payload = GithubGraphqlInput(
                    query = """query(${'$'}owner: String!, ${'$'}name: String!) {
    repository(owner: ${'$'}owner, name: ${'$'}name) {
        issues(first: 10) {
            nodes {
                id
                url
                title
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

                val issuesOutput = GithubApi.instance.fetchIssues(payload)
                val resultText = "${issuesOutput.data.repository.issues.nodes[0]}"
                val tmp = mutableListOf<String>()
                for (issue in issuesOutput.data.repository.issues.nodes) {
                    tmp.add(issue.url)
                }
                githubIssues.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, tmp)
                githubIssues.setOnItemClickListener { adapterView, view, i, l ->
                    adapterView.getItemAtPosition(i)
                }

                Log.i("api", resultText)
            } catch (e: Exception) {
                val resultText = e.message

                Log.w("api", resultText, e)
            }
        }
    }
}
