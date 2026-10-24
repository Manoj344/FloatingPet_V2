package com.example.floatingaipet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 64, 48, 48)
            setBackgroundColor(0xFF080D1B.toInt())
        }

        val title = TextView(this).apply {
            text = "Floating AI Pet"
            textSize = 28f
            setTextColor(0xFFFFFFFF.toInt())
        }
        val description = TextView(this).apply {
            text = "Your little digital companion lives above your other apps. Allow overlay access, then start your pet."
            textSize = 16f
            setTextColor(0xFFCAD5EA.toInt())
            setPadding(0, 20, 0, 32)
        }
        val start = Button(this).apply {
            text = "Start floating pet"
            setOnClickListener { startPet() }
        }
        val stop = Button(this).apply {
            text = "Stop floating pet"
            setOnClickListener {
                stopService(Intent(this@MainActivity, FloatingPetService::class.java))
                Toast.makeText(this@MainActivity, "Pet stopped", Toast.LENGTH_SHORT).show()
            }
        }
        val note = TextView(this).apply {
            text = "Tip: Drag the face to move it. Tap it to change its expression. Battery and charging events trigger automatic reactions."
            textSize = 14f
            setTextColor(0xFF8FA7CA.toInt())
            setPadding(0, 24, 0, 0)
        }

        layout.addView(title)
        layout.addView(description)
        layout.addView(start)
        layout.addView(stop)
        layout.addView(note)
        setContentView(layout)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }

    private fun startPet() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            Toast.makeText(this, "Allow display over other apps, then tap Start again.", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, FloatingPetService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Your pet is now floating!", Toast.LENGTH_SHORT).show()
    }
}
