package com.rexandel.cube_crush.data.managers

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.rexandel.cube_crush.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<Sound, Int>()
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    var isSoundEnabled = prefs.getBoolean("sound_enabled", true)
        private set

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        soundMap[Sound.BLOCK] = soundPool.load(context, R.raw.block, 1)
        soundMap[Sound.GAME_OVER] = soundPool.load(context, R.raw.game_over, 1)
        soundMap[Sound.LOGIN_SUCCESS] = soundPool.load(context, R.raw.login_success, 1)
        soundMap[Sound.LINE_CLEARED] = soundPool.load(context, R.raw.line_cleared, 1)
    }

    fun playSound(sound: Sound) {
        if (!isSoundEnabled) return
        
        val soundId = soundMap[sound] ?: return
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    fun toggleSound() {
        isSoundEnabled = !isSoundEnabled
        prefs.edit().putBoolean("sound_enabled", isSoundEnabled).apply()
    }

    fun release() {
        soundPool.release()
    }

    enum class Sound {
        BLOCK,
        GAME_OVER,
        LOGIN_SUCCESS,
        LINE_CLEARED
    }
    
    companion object {
        @Volatile
        private var INSTANCE: SoundManager? = null

        fun getInstance(context: Context): SoundManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SoundManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
