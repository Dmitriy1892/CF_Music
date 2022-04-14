package com.coldfier.cfmusic.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.coldfier.cfmusic.AlbumsScreen
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.use_case.model.Album

@Preview
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val pickedTabIndex = remember {
        mutableStateOf(1)
    }

    BottomSheetScaffold(
        topBar = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp)
            ) {
                MainTitle(stringResId = R.string.app_name)
                MainTabBar(pickedTabIndex = pickedTabIndex)
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box {
                PlayerExpanded(
                    currentFraction = bottomSheetScaffoldState.currentFraction,
                    bottomSheetState = bottomSheetScaffoldState.bottomSheetState
                )
                PlayerCollapsed(
                    currentFraction = bottomSheetScaffoldState.currentFraction,
                    bottomSheetState = bottomSheetScaffoldState.bottomSheetState
                )
            }

        },
        sheetPeekHeight = 110.dp
    ) {
        if (pickedTabIndex.value == 0) {
            //TODO - SHOW ALBUMS SCREEN
            AlbumsScreen(
                modifier = Modifier.fillMaxSize().padding(bottom = 110.dp),
                albumsList = mutableListOf<Album>().apply {
                    repeat(15) {
                        add(
                            Album(
                                albumName = "$it#AlbumName"
                            )
                        )
                    }
                },
                clickListener = { /*TODO*/ }
            )
        } else {
            //TODO - SHOW FOLDERS SCREEN
            FoldersScreen(
                modifier = Modifier.padding(bottom = 110.dp),
                foldersList = mutableListOf<SongFolder>()
                    .apply {
                        repeat(20) {
                            add(
                                SongFolder(
                                    folderName = "$it#folder",
                                    songsQuantity = it
                                )
                            )
                        }
                    },
                clickListener = { /*TODO*/ }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
val BottomSheetScaffoldState.currentFraction: Float
    get() {
        val fraction = bottomSheetState.progress.fraction
        val targetValue = bottomSheetState.targetValue
        val currentValue = bottomSheetState.currentValue

        return when {
            currentValue == BottomSheetValue.Collapsed
                    && targetValue == BottomSheetValue.Collapsed -> 0f

            currentValue == BottomSheetValue.Expanded
                    && targetValue == BottomSheetValue.Expanded -> 1f

            currentValue == BottomSheetValue.Collapsed
                    && targetValue == BottomSheetValue.Expanded -> fraction

            else -> 1f - fraction
        }
    }

@Composable
private fun MainTitle(@StringRes stringResId: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = stringResId),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically),
            fontFamily = SfFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color(0xFF091227),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun MainTabBar(pickedTabIndex: MutableState<Int>) {
    var tabIndex by remember { pickedTabIndex }
    val tabTitles = listOf(
        stringResource(id = R.string.albums_tab),
        stringResource(id = R.string.folders_tab)
    )
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = tabIndex,
        backgroundColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabIndex])
                    .fillMaxHeight()
                    .padding(all = 5.dp)
                    .background(
                        color = Color(0xFF8996B8),
                        shape = RoundedCornerShape(
                            corner = CornerSize(10.dp)
                        )
                    ),
                color = Color.Unspecified
            )
        },
        divider = {}
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.zIndex(1f),
                selected = tabIndex == index,
                onClick = { tabIndex = index },
                text = { Text(text = title) }
            )
        }
    }
}