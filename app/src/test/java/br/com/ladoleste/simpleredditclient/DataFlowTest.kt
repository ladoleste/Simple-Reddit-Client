package br.com.ladoleste.simpleredditclient

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ladoleste.simpleredditclient.Helpers.Companion.readFile
import br.com.ladoleste.simpleredditclient.app.Category
import br.com.ladoleste.simpleredditclient.dto.News
import br.com.ladoleste.simpleredditclient.dto.Thing
import br.com.ladoleste.simpleredditclient.model.Api
import br.com.ladoleste.simpleredditclient.viewmodel.MainViewModel
import com.google.gson.Gson
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class DataFlowTest {

    @Mock
    private lateinit var api: Api

    @Suppress("unused")
    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(api)
    }

    /**
     * This covers the data flow from the api to the activity, covering its transformations
     */
    @Test
    fun test_the_API_success_response() {

        `when`(api.getNews(Category.NEW.toString().toLowerCase())).thenReturn(getSuccessResponse())

        viewModel.getNews()

        assertNull(viewModel.error.value)
        val item = viewModel.items.value?.first() as News
        assertEquals("@EVLEAKS Nokia 1, Nokia 7+", item.title)
        assertEquals("iFonePhag", item.author)
        assertEquals(9, item.numComments)
        assertEquals(10, item.score)
    }

    @Test
    fun test_the_API_error_response() {

        `when`(api.getNews(Category.NEW.toString().toLowerCase())).thenReturn(getErrorResponse())
        viewModel.getNews()

        assertNull(viewModel.items.value)
        assertNotNull(viewModel.error.value)
    }

    private fun getSuccessResponse(): Single<Thing>? {
        return Single.just(Gson().fromJson(readFile(), Thing::class.java))
    }

    private fun getErrorResponse() = Single.error<Thing>(RuntimeException("Mock Test"))
}
