package com.serranoie.android.buybuddy.ui.quiz.questions

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.quiz.QuizViewModel
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.quiz.common.RadioButtonOption
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

data class Category(
    val categoryId: Int,
    @StringRes val name: Int,
)

@Composable
fun CategoryQuestion(
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    possibleAnswers: List<Category>,
    selectedAnswer: String?,
    onOptionSelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val viewModel: QuizViewModel = hiltViewModel()

    QuestionWrapper(
        titleResourceId = titleResourceId,
        directionsResourceId = directionsResourceId,
        modifier = modifier.selectableGroup(),
    ) {
        possibleAnswers.forEach { category ->
            RadioButtonOption(
                modifier = Modifier.padding(vertical = smallPadding),
                text = stringResource(id = category.name),
                selected = selectedCategory == category,
                onOptionSelected = {
                    selectedCategory = category
                    viewModel.onCategoryResponse(category)
                    onOptionSelected(category)
                },
                category = category,
            )
        }
    }
}

@Composable
fun PopulateCategoryQuestion(
    selectedAnswer: String,
    onOptionSelected: (Category) -> Unit,
    modifier: Modifier,
) {
    val possibleAnswers =
        listOf(
            Category(1, R.string.category_art),
            Category(2, R.string.category_automotive),
            Category(3, R.string.category_beauty),
            Category(4, R.string.category_books),
            Category(5, R.string.category_clothing),
            Category(6, R.string.category_electronics),
            Category(7, R.string.category_gaming),
            Category(8, R.string.category_hobbies),
            Category(9, R.string.category_tools),
        )

    CategoryQuestion(
        titleResourceId = R.string.category_question,
        directionsResourceId = R.string.category_helper,
        possibleAnswers = possibleAnswers,
        selectedAnswer = selectedAnswer,
        onOptionSelected = onOptionSelected,
        modifier = modifier,
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoryPreview() {
    val possibleAnswers =
        listOf(
            Category(1, R.string.category_art),
            Category(2, R.string.category_automotive),
            Category(3, R.string.category_beauty),
            Category(4, R.string.category_books),
            Category(5, R.string.category_clothing),
            Category(6, R.string.category_electronics),
            Category(7, R.string.category_gaming),
            Category(8, R.string.category_hobbies),
            Category(9, R.string.category_tools),
        )

    Surface {
        CategoryQuestion(
            titleResourceId = R.string.category_question,
            directionsResourceId = R.string.category_helper,
            possibleAnswers = possibleAnswers,
            selectedAnswer = "",
            onOptionSelected = {},
        )
    }
}
