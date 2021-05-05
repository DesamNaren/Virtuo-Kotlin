package com.cgg.virtuokotlin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cgg.virtuokotlin.source.CoOrdinates


/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(
    entities = [CoOrdinates::class],
    version = 1,
    exportSchema = false
)
abstract class VirtuoDatabase : RoomDatabase() {
    abstract fun dao(): CoOrdinateDao?

    companion object {
        private var INSTANCE: VirtuoDatabase? = null
        fun getDatabase(context: Context): VirtuoDatabase? {
            if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            VirtuoDatabase::class.java, "Virtuo.db"
                        ) // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build()
            }
            return INSTANCE
        }
    }
}