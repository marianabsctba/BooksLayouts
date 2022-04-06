package br.infnet.marianabs.layouts.ui.user.utils

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import br.infnet.marianabs.layouts.MainActivity
import java.util.concurrent.TimeUnit

class NotifyRepository {
    fun myNotification(mainActivity: MainActivity){
        val myWorkRequest= PeriodicWorkRequest
            .Builder(Worker::class.java,3, TimeUnit.DAYS)
            .setInputData(workDataOf(
                "title" to "Olá ",
                "message" to "encontre um livro instigante hoje... ")
            )
            .build()
        WorkManager.getInstance(mainActivity).enqueueUniquePeriodicWork(
            "periodicBookWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            myWorkRequest
        )
    }
}
