package ste.archykuroko.aguapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ste.archykuroko.aguapp.data.WaterDataStore

class ResetWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        WaterDataStore.saveVasos(applicationContext, 0)
        return Result.success()
    }
}
