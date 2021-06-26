package com.cgg.virtuokotlin.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cgg.virtuokotlin.db.VirtuoDatabase
import com.cgg.virtuokotlin.source.CoOrdinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashRepositoryTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context
    private var db: VirtuoDatabase? = null

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, VirtuoDatabase::class.java)
            .allowMainThreadQueries().build()
    }

    @Test
    fun callVersionAPI() {
        val list = mutableListOf<CoOrdinates>()
        val ordinates = CoOrdinates(
            1, "",
            "", "", "", "", true
        )
        list.add(ordinates)
        CoroutineScope(Dispatchers.IO).launch {
            db!!.dao()!!.insertCoOrdinates(list)
            val result = db!!.dao()!!.getCoOrdinates()
            if (result.isNotEmpty()) {
                Log.i("RESULT", "setUp: $result.size")
            }
            assert(result.isNotEmpty())
        }
    }

    @After
    fun tearDown(){
        db!!.close()
    }
}
