package com.serranoie.android.buybuddy.ui.quiz

import com.serranoie.android.buybuddy.ui.quiz.common.Questions

data class QuizScreenData(
    val questionIndex: Int,
    val questionCount: Int,
    val shouldShowPreviousButton: Boolean,
    val shouldShowDoneButton: Boolean,
    val currentQuestion: Questions,
)