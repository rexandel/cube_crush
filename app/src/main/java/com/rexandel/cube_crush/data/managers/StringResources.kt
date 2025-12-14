package com.rexandel.cube_crush.data.managers

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

    val settings: String
        @Composable get() = stringResource(R.string.settings)

    val back: String
        @Composable get() = stringResource(R.string.back)

    val profile: String
        @Composable get() = stringResource(R.string.profile)

    val notAvailable: String
        @Composable get() = stringResource(R.string.not_available)

    val theme: String
        @Composable get() = stringResource(R.string.theme)

    val changeTheme: String
        @Composable get() = stringResource(R.string.change_theme)

    val language: String
        @Composable get() = stringResource(R.string.language)

    val changeLanguage: String
        @Composable get() = stringResource(R.string.change_language)

    val changePassword: String
        @Composable get() = stringResource(R.string.change_password)

    val logout: String
        @Composable get() = stringResource(R.string.logout)

    val selectTheme: String
        @Composable get() = stringResource(R.string.select_theme)

    val darkTheme: String
        @Composable get() = stringResource(R.string.dark_theme)

    val lightTheme: String
        @Composable get() = stringResource(R.string.light_theme)

    val systemTheme: String
        @Composable get() = stringResource(R.string.system_theme)

    val selected: String
        @Composable get() = stringResource(R.string.selected)

    val cancel: String
        @Composable get() = stringResource(R.string.cancel)

    val selectLanguage: String
        @Composable get() = stringResource(R.string.select_language)

    val systemLanguage: String
        @Composable get() = stringResource(R.string.system_language)

    val edit: String
        @Composable get() = stringResource(R.string.edit)

    val changeEmail: String
        @Composable get() = stringResource(R.string.change_email)

    val newEmail: String
        @Composable get() = stringResource(R.string.new_email)

    val passwordForConfirmation: String
        @Composable get() = stringResource(R.string.password_for_confirmation)

    val save: String
        @Composable get() = stringResource(R.string.save)

    fun invalidPassword(context: Context): String = context.getString(R.string.invalid_password)
    fun emailAlreadyUsed(context: Context): String = context.getString(R.string.email_already_used)
    val currentPassword: String
        @Composable get() = stringResource(R.string.current_password)
    val newPassword: String
        @Composable get() = stringResource(R.string.new_password)
    val confirmNewPassword: String
        @Composable get() = stringResource(R.string.confirm_new_password)
    fun invalidCurrentPassword(context: Context): String = context.getString(R.string.invalid_current_password)
    fun passwordMinLength(context: Context): String = context.getString(R.string.password_min_length)
    fun passwordChangeError(context: Context): String = context.getString(R.string.password_change_error)
    fun logoutConfirmation(context: Context): String = context.getString(R.string.logout_confirmation)
    val highScoreLabel: String
        @Composable get() = stringResource(R.string.high_score_title)

    val nickname: String
        @Composable get() = stringResource(R.string.nickname)

    val changeNickname: String
        @Composable get() = stringResource(R.string.change_nickname)

    val newNickname: String
        @Composable get() = stringResource(R.string.new_nickname)

    fun nicknameTooShort(context: Context): String = context.getString(R.string.nickname_too_short)
    fun nicknameExists(context: Context): String = context.getString(R.string.nickname_exists)
    fun nicknameEmpty(context: Context): String = context.getString(R.string.nickname_empty)

    fun registrationFailed(context: Context): String = context.getString(R.string.registration_failed)

    val nicknameLabel: String
        @Composable get() = stringResource(R.string.nickname)

    val userMenu: String
        @Composable get() = stringResource(R.string.user_menu)

    fun getLoadingPreparing(context: Context): String = context.getString(R.string.loading_preparing)
    fun getLoadingCreatingBoard(context: Context): String = context.getString(R.string.loading_creating_board)
    fun getLoadingInitializingSystems(context: Context): String = context.getString(R.string.loading_initializing_systems)
    fun getLoadingComplete(context: Context): String = context.getString(R.string.loading_complete)
    fun getLoadingReady(context: Context): String = context.getString(R.string.loading_ready)
    fun getLoadingError(context: Context, message: String): String = context.getString(R.string.loading_error, message)
    val combo: String
        @Composable get() = stringResource(R.string.combo)

    val scoresScreenTitle: String
        @Composable get() = stringResource(R.string.scores_screen_title)

    val tabHistory: String
        @Composable get() = stringResource(R.string.tab_history)

    val tabTopPlayers: String
        @Composable get() = stringResource(R.string.tab_top_players)
}