package br.infnet.marianabs.layouts.ui.user.utils

import android.content.Context

import androidx.work.Worker
import androidx.work.WorkerParameters

class Worker(private val context: Context , workParams: WorkerParameters):
    Worker(context, workParams)  {

    override fun doWork(): Result {
        NotificationHelper(context = context).createNotification(inputData.getString("title")
            .toString() , inputData.getString("message").toString())
        return Result.success()
    }

}