package com.example.lesson_9_fokin.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.lesson_9_fokin.MainActivity
import com.example.lesson_9_fokin.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.net.URL
import java.util.zip.ZipFile

private const val newFile = "downloadFile"

class LoadZipService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        downloadFile(
            intent?.getStringExtra(KEY_URL).toString(),
        )
        startForeground(5, createNotification())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun downloadFile(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = URL(url).openConnection()
                val fileLength = connection.contentLength
                BufferedInputStream(connection.getInputStream()).use { input ->
                    openFileOutput(newFile, Context.MODE_PRIVATE).use { outputStream ->
                        val data = ByteArray(1024)
                        var total: Long = 0
                        var count: Int

                        while (input.read(data, 0, 1024).also { count = it } != -1) {
                            total += count
                            val status = (total * 100 / fileLength).toInt()
                            outputStream.write(data, 0, count)

                            val intent = Intent(MainActivity.STATUS_RECEIVER_ACTION).apply {
                                putExtra(KEY_STATUS, status)
                                putExtra(
                                    KEY_INFO,
                                    applicationContext.resources.getString(R.string.donload_file_process)
                                )
                            }
                            sendBroadcast(intent)
                        }
                    }
                }
                unZip()
            } catch (ex: Exception) {
                val intent = Intent(MainActivity.STATUS_RECEIVER_ACTION).apply {
                    putExtra(KEY_ERROR, ex.message.toString())
                }
                sendBroadcast(intent)
            }
        }
    }

    private fun unZip() {
        val path1 = "${applicationContext.filesDir}/$newFile"
        val path2 = "${applicationContext.filesDir}"
        var fileName = ""

        try {
            val zipFile = File(path1)
            val outputDir = File(path2)

            ZipFile(zipFile).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                        if (entry.isDirectory) {
                            val d = File(outputDir, entry.name)
                            if (!d.exists()) d.mkdirs()
                        } else {
                            val f = File(outputDir, entry.name)
                            fileName = f.name
                            if (f.parentFile?.exists() != true) f.parentFile?.mkdirs()

                            f.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            zipFile.delete()

            val intent = Intent(MainActivity.STATUS_RECEIVER_ACTION).apply {
                putExtra(KEY_COMPLETE, 1)
                putExtra(KEY_PATH, "$path2/$fileName")
            }
            sendBroadcast(intent)
        } catch (ex: Exception) {
            val intent = Intent(MainActivity.STATUS_RECEIVER_ACTION).apply {
                putExtra(KEY_ERROR, ex.message.toString())
            }
            sendBroadcast(intent)
        }
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(Color.BLUE)
            .setContentTitle("Download file")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    companion object {

        const val KEY_URL = "key_url"

        const val KEY_ERROR = "key_error"

        const val KEY_INFO = "key_info"

        const val KEY_STATUS = "key_status"

        const val KEY_COMPLETE = "key_complete"

        const val KEY_PATH = "key_path"

        const val CHANNEL_ID = "channel_id"

        fun createStartIntent(context: Context, url: String): Intent {
            return Intent(context, LoadZipService::class.java).apply {
                putExtra(KEY_URL, url)
            }
        }
    }
}