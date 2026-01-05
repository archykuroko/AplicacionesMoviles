package ste.archykuroko.examenfinal.data

import android.content.Context
import androidx.room.Room

object DbProvider {
    @Volatile private var db: AppDb? = null

    fun get(context: Context): AppDb {
        return db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java,
                "examen_final_db"
            ).build().also { db = it }
        }
    }
}
