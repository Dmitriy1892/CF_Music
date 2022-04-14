package com.coldfier.cfmusic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coldfier.cfmusic.ui.SfFontFamily
import com.coldfier.cfmusic.use_case.model.Album

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    albumsList: List<Album>,
    clickListener: (album: Album) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        albumsList.forEach { album ->
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple(bounded = true)
                        ) {
                            clickListener(album)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(
                                top = 10.dp,
                                start = 5.dp,
                                end = 5.dp
                            )
                            .size(100.dp, 100.dp)
                            .clip(RoundedCornerShape(corner = CornerSize(15.dp))),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(album.albumImageUri)
                            .placeholder(R.drawable.bg_album_placeholder)
                            .error(R.drawable.bg_album_placeholder)
                            .crossfade(true)
                            .build(),
                        contentDescription = null
                    )

                    Text(
                        text = album.albumName ?: "",
                        modifier = Modifier
                            .padding(
                                top = 5.dp,
                                bottom = 5.dp,
                                start = 10.dp,
                                end = 10.dp
                            ),
                        color = Color(0xFF0E172D),
                        fontFamily = SfFontFamily,
                        fontStyle = FontStyle.Normal,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AlbumsScreenPreview() {
    AlbumsScreen(
        modifier = Modifier.fillMaxSize(),
        albumsList = mutableListOf<Album>().apply {
            repeat(15) {
                add(
                    Album(
                        albumName = "$it#AlbumName"
                    )
                )
            }
        },
        clickListener = {}
    )
}