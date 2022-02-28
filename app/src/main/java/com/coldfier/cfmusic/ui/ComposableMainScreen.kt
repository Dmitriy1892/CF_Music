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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.coldfier.cfmusic.R

@Preview
@Composable
fun MainScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        MainTitle(stringResId = R.string.app_name)
        MainTabBar(pickedTabIndex = 0)
    }
}

@Composable
fun MainTitle(@StringRes stringResId: Int) {
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
            fontFamily = FontFamily(Font(R.font.sf_font_bold)),
            fontSize = 20.sp,
            color = Color(0xFF091227),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun MainTabBar(pickedTabIndex: Int) {
    var tabIndex by remember { mutableStateOf(pickedTabIndex) }
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
        }
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