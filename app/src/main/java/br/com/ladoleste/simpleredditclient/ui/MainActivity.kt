package br.com.ladoleste.simpleredditclient.ui

import android.app.ActivityOptions
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import br.com.ladoleste.simpleredditclient.R
import br.com.ladoleste.simpleredditclient.app.Category
import br.com.ladoleste.simpleredditclient.app.NewsItem
import br.com.ladoleste.simpleredditclient.app.Util.X.getErrorMessage
import br.com.ladoleste.simpleredditclient.ui.adapter.NewsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.inc_toolbar.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), ItemClick {
    private lateinit var model: MainViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var loadingScrollListener: LoadingScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "${getString(R.string.app_title)} [${model.category}]"
        swiperefresh.setOnRefreshListener {
            model.getNews(true)
        }

        model.items.observe(this, Observer<List<NewsItem>> {
            loading.visibility = View.GONE

            rvListing.clearOnScrollListeners()
            if (model.loadingEnabled)
                rv_listing.addOnScrollListener(loadingScrollListener)

            supportActionBar?.title = "${getString(R.string.app_title)} [${model.category}]"
            it?.let {
                swiperefresh.isRefreshing = false
                if (rvListing.adapter == null) {
                    newsAdapter = NewsAdapter(it, this)
                    rvListing.adapter = newsAdapter
                    loadingScrollListener.loading = false
                } else {
                    loadingScrollListener.loading = false
                    newsAdapter.updateItems(it)
                }
            }
        })

        model.error.observe(this, Observer {
            Timber.e(it)
            swiperefresh.isRefreshing = false
            loading.visibility = View.GONE
            Snackbar.make(root_view, getErrorMessage(it), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { loading.visibility = View.VISIBLE; model.getNews() }
                    .show()
        })

        model.getNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        model.category = when (item.itemId) {
            R.id.new_ -> Category.NEW
            R.id.top -> Category.TOP
            else -> Category.HOT
        }

        loading.visibility = View.VISIBLE
        model.getNews(true)

        return true
    }

    override fun onItemClick(id: String, image: ImageView?) {
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("id", id)

        if (image != null) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, image, "name")
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private val rvListing by lazy {
        val linearLayoutManager = LinearLayoutManager(this)
        rv_listing.layoutManager = linearLayoutManager
        loadingScrollListener = LoadingScrollListener({ model.getNews() }, linearLayoutManager)
        rv_listing
    }
}
