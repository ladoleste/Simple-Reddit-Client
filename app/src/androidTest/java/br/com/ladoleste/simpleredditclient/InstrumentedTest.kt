package br.com.ladoleste.simpleredditclient

import android.content.Intent
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.ladoleste.simpleredditclient.common.CustomApplication.Companion.apiUrl
import br.com.ladoleste.simpleredditclient.common.Util
import br.com.ladoleste.simpleredditclient.ui.MainActivity
import br.com.ladoleste.simpleredditclient.ui.adapter.NewsAdapter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun navigate_to_original_article() {

        val server = MockWebServer()
        val list = Util.readFileFromAssets(InstrumentationRegistry.getContext(), "responseList.json")
        val listPage2 = Util.readFileFromAssets(InstrumentationRegistry.getContext(), "responseList2.json")
        val details = Util.readFileFromAssets(InstrumentationRegistry.getContext(), "responseDetails.json")

        server.enqueue(MockResponse().setBody(list))
        server.enqueue(MockResponse().setBody(listPage2))
        server.enqueue(MockResponse().setBody(details))
        server.start()

        apiUrl = server.url("/test/").toString()

        activityRule.launchActivity(Intent())

        onView(withText("Simple Reddit Client [NEW]")).check(matches(isDisplayed()))

        onView(withId(R.id.rv_listing))
                .perform(RecyclerViewActions.scrollToPosition<NewsAdapter.ViewHolder>(9))

        onView(withId(R.id.rv_listing))
                .perform(swipeUp())

        SystemClock.sleep(500)

        onView(withId(R.id.rv_listing))
                .perform(RecyclerViewActions.actionOnItemAtPosition<NewsAdapter.ViewHolder>(14, click()))

        onView(withText(R.string.act_comments)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_open_article)).perform(click())
    }
}
