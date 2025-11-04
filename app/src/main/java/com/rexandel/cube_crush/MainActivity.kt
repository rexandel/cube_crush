package com.rexandel.cube_crush

import android.app.Activity
import android.os.Bundle
import com.rexandel.cube_crush.ui.screens.GameScreen

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameScreen(this))
    }
}
