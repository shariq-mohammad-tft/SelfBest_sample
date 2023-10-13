package com.tft.selfbest.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.tft.selfbest.SelfBestApplication
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.SelfBestRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: SelfBestRepository
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "SyncDataWorker"

        fun submitSyncDataWorker(app: SelfBestApplication) {
            val periodicWorkRequest: PeriodicWorkRequest =
                PeriodicWorkRequestBuilder<SyncDataWorker>(
                    30, TimeUnit.MINUTES
                )
                    .addTag(TAG)
                    .build()
            WorkManager.getInstance(app).enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest
            )
        }
    }

    override suspend fun doWork(): Result = coroutineScope {
        var isSyncFail = false
        repo.syncData(this)
            .onStart {
                Log.d(TAG, "Syncing started")
            }.onCompletion {
                Log.d(TAG, "Syncing completed")
            }
            .collect { response ->
                if (response is NetworkResponse.Error) {
                    isSyncFail = true
                }
            }
        if (!isSyncFail)
            Result.success()
        else
            Result.retry()
    }
}