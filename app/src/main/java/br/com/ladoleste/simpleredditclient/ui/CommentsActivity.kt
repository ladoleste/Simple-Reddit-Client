package br.com.ladoleste.simpleredditclient.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.ladoleste.simpleredditclient.R
import br.com.ladoleste.simpleredditclient.app.CustomApplication.Companion.drawableHolder
import br.com.ladoleste.simpleredditclient.app.Util.X.getBitmapFromVectorDrawable
import br.com.ladoleste.simpleredditclient.app.Util.X.getErrorMessage
import br.com.ladoleste.simpleredditclient.app.loadImage
import br.com.ladoleste.simpleredditclient.app.toFriendlyTime
import br.com.ladoleste.simpleredditclient.app.toHtml
import br.com.ladoleste.simpleredditclient.ui.adapter.CommentsAdapter
import br.com.ladoleste.simpleredditclient.ui.adapter.getCommentsText
import br.com.ladoleste.simpleredditclient.ui.customtabs.CustomTabsHelper
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.inc_info.*
import kotlinx.android.synthetic.main.inc_toolbar.*
import timber.log.Timber

class CommentsActivity : AppCompatActivity() {
    private lateinit var model: CommentsViewModel
    private lateinit var customTabsIntent: CustomTabsIntent
    private val customTabsHelper = CustomTabsHelper()
    private var uri: Uri? = null
    private var shareLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        image_view.setImageDrawable(drawableHolder)
        if (drawableHolder == null)
            image_view.visibility = View.GONE
        else
            drawableHolder = null

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.act_comments)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.extras.getString("id")

        model = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        model.id = id

        model.comments.observe(this, Observer {
            loading.visibility = View.GONE
            it?.let {
                if (it.isEmpty()) {
                    tv_no_comments.visibility = View.VISIBLE
                } else
                    rvComments.adapter = CommentsAdapter(it.take(15))
            }

            model.news.observe(this, Observer {
                it?.apply {
                    uri = Uri.parse(url)
                    shareLink = permalink
                    tv_title.text = title
                    tv_avg.text = score.toString()
                    tv_created.text = createdUtc.toFriendlyTime()
                    if (isSelf) {
                        tv_text.visibility = View.VISIBLE
                        tv_text.text = selftextHtml?.toHtml()
                    } else {
                        if (preview != null) {
                            Handler().postDelayed({ image_view.loadImage(preview.images.first().source.url) }, 500)
                        }
                    }
                    tv_num_comments.text = getCommentsText(numComments)
                    inc_info.visibility = View.VISIBLE

                    val prefech = customTabsHelper.mayLaunchUrl(uri)
                    Timber.i("Pre-fetch success: $prefech")

                    tv_open_article.setOnClickListener {
                        CustomTabsHelper.openCustomTab(this@CommentsActivity, customTabsIntent, uri, null)
                    }
                }
            })
        })

        model.error.observe(this, Observer {
            Timber.e(it)

            loading.visibility = View.GONE
            Snackbar.make(root_view, getErrorMessage(it), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { loading.visibility = View.VISIBLE;model.getComments() }
                    .show()
        })

        model.getComments()

        val backArrow = getBitmapFromVectorDrawable(R.drawable.ic_arrow_back_white)

        customTabsIntent = CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setShowTitle(true)
                .setCloseButtonIcon(backArrow)
                .setToolbarColor(ContextCompat.getColor(this@CommentsActivity, R.color.colorPrimary))
                .setStartAnimations(this@CommentsActivity, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(this@CommentsActivity, R.anim.slide_in_left, R.anim.slide_out_right)
                .build()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_comments, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.reddit.com" + shareLink)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.open -> CustomTabsHelper.openCustomTab(this@CommentsActivity, customTabsIntent, uri, null)

            else -> onBackPressed()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        customTabsHelper.bindCustomTabsService(this)
    }

    override fun onPause() {
        customTabsHelper.unbindCustomTabsService(this)
        super.onPause()
    }

    private val rvComments by lazy {
        val linearLayoutManager = LinearLayoutManager(this)
        rv_comments.layoutManager = linearLayoutManager
        rv_comments.isNestedScrollingEnabled = false
        rv_comments
    }
}