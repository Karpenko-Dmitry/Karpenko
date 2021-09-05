package dmitry.karpenko.data.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dmitry.karpenko.data.database.dao.CurrentUserGifPositionDao
import dmitry.karpenko.data.database.dao.GifDao
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.data.database.entity.CurrentUserGifPosition
import dmitry.karpenko.data.database.entity.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Gif::class, CurrentUserGifPosition::class],version = 1,exportSchema = false)
abstract class GifDatabase : RoomDatabase() {

    abstract val gifDao : GifDao
    abstract val currentUserGifPositionDao : CurrentUserGifPositionDao

    companion object {
        @Volatile
        private var INSTANCE: GifDatabase? = null

        fun getDatabase(context: Context) : GifDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GifDatabase::class.java,
                        "gif_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                //заполнение Таблицы CurrentGifPosition начальными данными при создании таблицы
                                val dao = getDatabase(context).currentUserGifPositionDao
                                CoroutineScope(Dispatchers.IO).launch {
                                    Category.values().forEach {
                                        dao.insert(CurrentUserGifPosition(it.id,0))
                                    }
                                }
                            }
                        })
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}