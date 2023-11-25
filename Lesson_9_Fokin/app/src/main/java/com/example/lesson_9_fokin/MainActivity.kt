package com.example.lesson_9_fokin

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_9_fokin.data.model.WeatherResult
import com.example.lesson_9_fokin.databinding.ActivityMainBinding
import com.example.lesson_9_fokin.service.LoadWeatherService
import com.example.lesson_9_fokin.service.LoadZipService
import com.example.lesson_9_fokin.service.ServiceCallback
import com.squareup.picasso.Picasso
import java.io.File

private const val URL =
    "https://dl.dropboxusercontent.com/scl/fi/lhpo0z7cb7y0jqmu8km7o/in_the_space_514627.zip?rlkey=a5io44601c59kl7de8m9b2v1n&dl=0"

class MainActivity : AppCompatActivity(), ServiceCallback {

    private val binding by viewBinding(ActivityMainBinding::bind)

    private val serviceIntent by lazy { Intent(this, LoadWeatherService::class.java) }

    private var myBinder: LoadWeatherService.MyBinder? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myBinder = service as? LoadWeatherService.MyBinder
            myBinder?.service?.serviceCallback = this@MainActivity
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            myBinder = null
        }
    }

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getIntExtra(LoadZipService.KEY_STATUS, 0)
            val error = intent?.getStringExtra(LoadZipService.KEY_ERROR)
            val info = intent?.getStringExtra(LoadZipService.KEY_INFO)
            val path = intent?.getStringExtra(LoadZipService.KEY_PATH)

            if (intent?.getIntExtra(LoadZipService.KEY_COMPLETE, 0) == 1) {
                binding.layoutDownload.isVisible = false
                binding.materialButtonDownloadFile.isVisible = true

                Picasso.with(context).load(File(path.toString())).into(binding.imageViewNewFile)
                binding.imageViewNewFile.isVisible = true

                stopService(
                    LoadZipService.createStartIntent(
                        binding.root.context,
                        URL
                    )
                )
            }

            if (status != null && status != 0) {
                binding.imageViewNewFile.isVisible = false
                binding.materialButtonDownloadFile.isVisible = false
                binding.textViewErrorDownload.isVisible = false
                binding.layoutDownload.isVisible = true
                binding.textViewInfoDownload.text = info
                binding.linearProgressIndicator.setProgressCompat(status, true)

                if (status == 100) {
                    binding.textViewInfoDownload.text =
                        context?.resources?.getString(R.string.unzip)
                }
            }
            if (error != null) {
                binding.materialButtonDownloadFile.isVisible = false
                binding.textViewErrorDownload.text = error
                binding.textViewErrorDownload.isVisible = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.progressBar.isVisible = true
        bindService(serviceIntent, connection, BIND_AUTO_CREATE)

        binding.materialButtonDownloadFile.setOnClickListener {
            startDownloadService()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun startDownloadService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(
                LoadZipService.createStartIntent(
                    this,
                    URL
                )
            )
        } else {
            startService(
                LoadZipService.createStartIntent(
                    this,
                    URL
                )
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                downloadReceiver,
                IntentFilter(STATUS_RECEIVER_ACTION),
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(downloadReceiver, IntentFilter(STATUS_RECEIVER_ACTION))
        }
    }

    override fun onDestroy() {
        if (myBinder != null) {
            unbindService(connection)
        }
        super.onDestroy()
    }

    override fun bindWeather(weather: WeatherResult) {
        binding.layoutWeatherResult.isVisible = true
        if (weather.name == null) {
            binding.layoutWeatherResult.isVisible = false
            binding.textViewError.text = resources.getString(R.string.error, "Нет данных")
        } else {
            binding.textViewWeatherName.text =
                resources.getString(R.string.weather_name, weather.name)

            binding.textViewWeatherTemp.text =
                resources.getString(R.string.temp, weather.main?.temp)

            binding.textViewWeatherTempMinMax.text =
                resources.getString(
                    R.string.max_min_temp,
                    weather.main?.tempMax,
                    weather.main?.tempMin
                )

            binding.textViewWeatherWindSpeed.text = resources.getString(
                R.string.speed_wind,
                weather.wind?.speed.toString()
            )

            binding.textViewWeatherCloudy.text =
                resources.getString(R.string.cloudy, weather.clouds?.all)

            weather.weather?.size?.let {
                repeat(it) { index ->
                    binding.textViewWeatherMain.text =
                        resources.getString(R.string.main, weather.weather[index].main)
                }
            }
        }
    }

    override fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    override fun sendError(error: String) {
        binding.textViewError.text = error
        binding.textViewError.isVisible = true
        binding.layoutWeatherResult.isVisible = false
    }

    companion object {
        const val STATUS_RECEIVER_ACTION = "com.example.lesson_9_fokin.STATUS_RECEIVER_ACTION"
    }
}