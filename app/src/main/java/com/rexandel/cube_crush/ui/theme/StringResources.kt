package com.rexandel.cube_crush.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rexandel.cube_crush.R

object StringResources {
    val appName: String
        @Composable get() = stringResource(R.string.app_name)

    @Composable
    fun highScore(score: Int): String = stringResource(R.string.high_score, score)

    val pause: String
        @Composable get() = stringResource(R.string.pause)

    val gameOver: String
        @Composable get() = stringResource(R.string.game_over)

    @Composable
    fun score(points: Int): String = stringResource(R.string.score, points)

    val restart: String
        @Composable get() = stringResource(R.string.restart)

    val exitToMenu: String
        @Composable get() = stringResource(R.string.exit_to_menu)

    val resume: String
        @Composable get() = stringResource(R.string.resume)

    val crownIcon: String
        @Composable get() = stringResource(R.string.crown_icon)

    val pauseIcon: String
        @Composable get() = stringResource(R.string.pause_icon)

    val gamePaused: String
        @Composable get() = stringResource(R.string.game_paused)

    val newGame: String
        @Composable get() = stringResource(R.string.new_game)

    val share: String
        @Composable get() = stringResource(R.string.share)

    val shareIcon: String
        @Composable get() = stringResource(R.string.share_icon)

    @Composable
    fun yourScore(score: Int): String = stringResource(R.string.your_score, score)

    @Composable
    fun record(highScore: Int): String = stringResource(R.string.record, highScore)

    fun shareTextNewRecord(context: Context, score: Int): String = context.getString(R.string.share_text_new_record, score)

    fun shareTextRegular(context: Context, score: Int, highScore: Int): String = context.getString(R.string.share_text_regular, score, highScore)

    fun shareDialogTitle(context: Context): String = context.getString(R.string.share_dialog_title)

    val gameLogo: String
        @Composable get() = stringResource(R.string.game_logo)

    val gameDescription: String
        @Composable get() = stringResource(R.string.game_description)

    val startGame: String
        @Composable get() = stringResource(R.string.start_game)

    val exit: String
        @Composable get() = stringResource(R.string.exit)

    val login: String
        @Composable get() = stringResource(R.string.login)

    val register: String
        @Composable get() = stringResource(R.string.register)

    val email: String
        @Composable get() = stringResource(R.string.email)

    val password: String
        @Composable get() = stringResource(R.string.password)

    val confirmPassword: String
        @Composable get() = stringResource(R.string.confirm_password)

    val loginButton: String
        @Composable get() = stringResource(R.string.login_button)

    val registerButton: String
        @Composable get() = stringResource(R.string.register_button)

    val noAccount: String
        @Composable get() = stringResource(R.string.no_account)

    val haveAccount: String
        @Composable get() = stringResource(R.string.have_account)

    fun fillAllFields(context: Context): String = context.getString(R.string.fill_all_fields)
    fun invalidEmailPassword(context: Context): String = context.getString(R.string.invalid_email_password)
    fun passwordTooShort(context: Context): String = context.getString(R.string.password_too_short)
    fun passwordsDontMatch(context: Context): String = context.getString(R.string.passwords_dont_match)
    fun userExists(context: Context): String = context.getString(R.string.user_exists)
}