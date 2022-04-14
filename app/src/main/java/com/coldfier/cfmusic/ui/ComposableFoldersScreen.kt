package com.coldfier.cfmusic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.data.database_room.model.SongFolder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoldersScreen(
    modifier: Modifier = Modifier,
    foldersList: List<SongFolder>,
    clickListener: (folder: SongFolder) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
    ) {
        foldersList.forEach { folder ->
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple(bounded = true)
                        ) { clickListener(folder) }
                    ) {
                        Box(modifier = Modifier
                            .width(100.dp)
                            .align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_folder),
                                contentDescription = folder.folderName ?: "",
                                modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.Center)
                            )

                            Text(
                                text = folder.songsQuantity?.toString() ?: "0",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF6B705C),
                                fontFamily = FontFamily(
                                    Font(R.font.sf_font_regular)
                                ),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Text(
                            text = folder.folderName ?: "",
                            textAlign = TextAlign.Center,
                            color = Color(0xFF0E172D),
                            fontFamily = FontFamily(
                                Font(R.font.sf_font_regular)
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreen() {
    FoldersScreen(foldersList = listOf(SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),SongFolder("name", 5),), clickListener = {})
}