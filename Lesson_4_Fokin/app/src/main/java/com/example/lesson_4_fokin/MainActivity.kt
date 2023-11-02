package com.example.lesson_4_fokin

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.lesson_4_fokin.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(LayoutInflater.from(this)) }
    private val itemsAdapter = ItemsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position >= 6) 2 else 1
            }
        }

        binding.recycleViewItems.layoutManager = layoutManager

        val customItemDecoration = CustomItemDecoration(16)
        binding.recycleViewItems.addItemDecoration(customItemDecoration)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.materialToolbar.menu.findItem(R.id.menuInfoButton).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.alert_dialog))
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            true
        }

        binding.materialToolbar.menu.findItem(R.id.menuHomeButton).setOnMenuItemClickListener {
            Toast.makeText(this@MainActivity, getString(R.string.press_home), Toast.LENGTH_SHORT).show()
            true
        }

        binding.recycleViewItems.adapter = itemsAdapter.apply {
            detailListener = DetailCardListener {
                Snackbar.make(binding.root, it.title, Snackbar.LENGTH_SHORT).show()
            }
            baseListener = BaseCardListener {
                Snackbar.make(binding.root, it.title, Snackbar.LENGTH_SHORT).show()
            }
        }

        itemsAdapter.setList(
            listOf(
                ListItem.DetailItem(DetailCard("Квитанции", "-40 080,55 Р", R.drawable.img_receipt)),
                ListItem.DetailItem(DetailCard("Счётчики", "Подайте показания", R.drawable.img_counter)),
                ListItem.DetailItem(DetailCard("Рассрочка", "Сл. платеж 25.02.2018", R.drawable.img_installment)),
                ListItem.DetailItem(DetailCard("Страхование", "Полис до 12.01.2019", R.drawable.img_insurance)),
                ListItem.DetailItem(DetailCard("Интернет и ТВ", "Баланс 350 Р", R.drawable.img_network)),
                ListItem.DetailItem(DetailCard("Домофон", "Подключен", R.drawable.img_intercom)),
                ListItem.DetailItem(DetailCard("Охрана", "Нет", R.drawable.img_security)),
                ListItem.BaseItem(BaseCard("Контакты УК и служб", R.drawable.img_contacts)),
                ListItem.BaseItem(BaseCard("Мои заявки", R.drawable.img_applications)),
                ListItem.BaseItem(BaseCard("Памятка жителя А101", R.drawable.img_memo))
            )
        )
    }

}