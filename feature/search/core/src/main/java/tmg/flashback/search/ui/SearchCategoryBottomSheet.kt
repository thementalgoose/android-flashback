@file:OptIn(ExperimentalMaterialApi::class)

package tmg.flashback.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.flashback.search.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.input.InputSelection
import tmg.flashback.ui.bottomsheet.ModalSheet
import tmg.flashback.ui.components.layouts.BottomSheet

@Composable
fun SearchCategoryBottomSheet(
    visible: Boolean = false,
    dismiss: () -> Unit,
    selected: SearchCategory?,
    categoryClicked: (SearchCategory) -> Unit
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = {
            if (!it) {
                dismiss()
            }
        },
        cancelable = true
    ) {
        BottomSheet(
            title = stringResource(id = R.string.search_category_option_title),
            subtitle = stringResource(id = R.string.search_category_option_description)
        ) {
            Column(Modifier.padding(horizontal = AppTheme.dimens.medium)) {
                SearchCategory.values().forEach {
                    InputSelection(
                        label = stringResource(id = it.label),
                        icon = it.icon,
                        isChecked = it == selected,
                        itemClicked = {
                            categoryClicked(it)
                        }
                    )
                }
            }
        }
    }
}