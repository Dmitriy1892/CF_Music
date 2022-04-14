package com.coldfier.cfmusic.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coldfier.cfmusic.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerExpanded(
    currentFraction: Float,
    bottomSheetState: BottomSheetState
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .graphicsLayer(alpha = currentFraction)
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 5.dp,
            backgroundColor = Color.White
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.collapse()
                        }
                    },
                    enabled = bottomSheetState.isExpanded
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_collapse_arrow),
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.fragment_player_title),
                    fontFamily = SfFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF091227),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        AsyncImage(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(260.dp, 260.dp)
                .clip(RoundedCornerShape(corner = CornerSize(10.dp)))
                .align(Alignment.CenterHorizontally),
            model = ImageRequest.Builder(LocalContext.current)
                .data(null /*TODO ADD URI */)
                .error(R.drawable.bg_song_placeholder)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.bg_song_placeholder),
            contentDescription = null
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)) {
            Text(
                text = "Song Name Song Name Song Name Song Name Song Name Song Name ", //TODO - REPLACE TEXT
                modifier = Modifier
                    .padding(start = 60.dp, end = 60.dp)
                    .align(Alignment.Center),
                fontFamily = SfFontFamily,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF091127),
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            StatefulImageButton(
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
                onClick = { /*TODO*/ },
                buttonImageResStateOn = R.drawable.ic_favorite_on,
                buttonImageResStateOff = R.drawable.ic_favorite_off,
                buttonSize = 34.dp,
                isStateOn = remember { mutableStateOf(false) }, //TODO - REPLACE
                enabled = bottomSheetState.isExpanded
            )
        }

        Text(
            text = "Artist Name Artist Name Artist Name Artist Name Artist Name ", //TODO - REPLACE TEXT
            modifier = Modifier
                .padding(top = 6.dp, start = 60.dp, end = 60.dp)
                .align(Alignment.CenterHorizontally),
            fontFamily = SfFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF8996B8),
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Box(modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()) {
            StatefulImageButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 30.dp),
                onClick = { /*TODO*/ },
                buttonImageResStateOn = R.drawable.ic_volume_on,
                buttonImageResStateOff = R.drawable.ic_volume_off,
                buttonSize = 34.dp,
                isStateOn = remember {
                    mutableStateOf(true)  //TODO - REPLACE
                },
                enabled = bottomSheetState.isExpanded
            )

            StatefulImageButton(
                modifier = Modifier
                    .padding(end = 80.dp)
                    .align(Alignment.CenterEnd),
                onClick = { /*TODO*/ },
                buttonImageResStateOn = R.drawable.ic_repeat_one,
                buttonImageResStateOff = R.drawable.ic_repeat_all,
                buttonSize = 34.dp,
                isStateOn = remember {
                    mutableStateOf(false) //TODO - REPLACE
                },
                enabled = bottomSheetState.isExpanded
            )

            StatefulImageButton(
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
                onClick = { /*TODO*/ },
                buttonImageResStateOn = R.drawable.ic_shuffle_on,
                buttonImageResStateOff = R.drawable.ic_shuffle_off,
                buttonSize = 34.dp,
                isStateOn = remember {
                    mutableStateOf(false)  //TODO - REPLACE
                },
                enabled = bottomSheetState.isExpanded
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .align(Alignment.CenterStart),
                text = "01:13",  //TODO - REPLACE TEXT
                fontFamily = SfFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF0E172D),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
                text = "-03:11", //TODO - REPLACE TEXT
                fontFamily = SfFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF0E172D),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        //TODO - REPLACE SLIDER VALUE
        var sliderValue by remember {
            mutableStateOf(0f)
        }

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 25.dp, end = 25.dp),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF091227),
                activeTrackColor = Color(0xFF091227),
                inactiveTrackColor = Color(0xFFD3D7DF)
            )
        )

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center
        ) {
            PlayerSkipTrackButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                onClick = {
                    // TODO - ADD LOGIC FOR NEW TRACK LISTENING
                },
                pressedButtonImageRes = R.drawable.ic_previous_song_on,
                unpressedButtonImageRes = R.drawable.ic_previous_song_off,
                buttonSize = 30.dp,
                enabled = bottomSheetState.isExpanded
            )

            PlayerPlayPauseButton(
                modifier = Modifier
                    .padding(start = 40.dp, end = 40.dp)
                    .align(Alignment.CenterVertically),
                onClick = { /*TODO*/ },
                buttonSize = 40.dp,
                enabled = bottomSheetState.isExpanded
            )

            PlayerSkipTrackButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                onClick = {
                    /*TODO*/
                },
                pressedButtonImageRes = R.drawable.ic_next_song_on,
                unpressedButtonImageRes = R.drawable.ic_next_song_off,
                buttonSize = 30.dp,
                enabled = bottomSheetState.isExpanded
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PlayerExpandedPreview() {
    PlayerExpanded(
        currentFraction = 1f,
        rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerCollapsed(
    currentFraction: Float,
    bottomSheetState: BottomSheetState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .graphicsLayer(alpha = 1f - currentFraction)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(10.dp)
                .size(90.dp, 90.dp)
                .clip(RoundedCornerShape(corner = CornerSize(10.dp))),
            model = ImageRequest.Builder(LocalContext.current)
                .data(null /*TODO ADD URI */)
                .error(R.drawable.bg_song_placeholder)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.bg_song_placeholder),
            contentDescription = null
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Song Name Song Name Song Name Song Name Song Name Song Name ",
                        fontFamily = SfFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF091127),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = "Artist Name Artist Name Artist Name Artist Name Artist Name ",
                        fontFamily = SfFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF8996B8),
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                PlayerSkipTrackButton(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.CenterVertically),
                    onClick = {
                        // TODO - ADD LOGIC FOR NEW TRACK LISTENING
                    },
                    pressedButtonImageRes = R.drawable.ic_previous_song_on,
                    unpressedButtonImageRes = R.drawable.ic_previous_song_off,
                    buttonSize = 30.dp,
                    enabled = bottomSheetState.isCollapsed
                )

                PlayerPlayPauseButton(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .align(Alignment.CenterVertically),
                    onClick = { /*TODO*/ },
                    buttonSize = 30.dp,
                    enabled = bottomSheetState.isCollapsed
                )

                PlayerSkipTrackButton(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterVertically),
                    onClick = {
                        /*TODO*/
                    },
                    pressedButtonImageRes = R.drawable.ic_next_song_on,
                    unpressedButtonImageRes = R.drawable.ic_next_song_off,
                    buttonSize = 30.dp,
                    enabled = bottomSheetState.isCollapsed
                )
            }

            var sliderValue by remember {
                mutableStateOf(0f)
            }

            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                value = sliderValue,
                onValueChange = { sliderValue = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF091227),
                    activeTrackColor = Color(0xFF091227),
                    inactiveTrackColor = Color(0xFFD3D7DF)
                ),
                enabled = bottomSheetState.isCollapsed
            )
        }
    }
}

@Composable
private fun PlayerSkipTrackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes pressedButtonImageRes: Int,
    @DrawableRes unpressedButtonImageRes: Int,
    buttonSize: Dp,
    enabled: Boolean
) {
    StatelessImageButton(
        modifier = modifier,
        onClick = onClick,
        pressedButtonImageRes = pressedButtonImageRes,
        unpressedButtonImageRes = unpressedButtonImageRes,
        buttonSize = buttonSize,
        enabled = enabled
    )
}

@Composable
private fun PlayerPlayPauseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes playButtonImageRes: Int = R.drawable.ic_play,
    @DrawableRes pauseButtonImageRes: Int = R.drawable.ic_pause,
    buttonSize: Dp,
    enabled: Boolean
//    playerViewModel: PlayerViewModel
) {
    StatefulImageButton(
        modifier = modifier,
        onClick = onClick,
        buttonImageResStateOn = playButtonImageRes,
        buttonImageResStateOff = pauseButtonImageRes,
        buttonSize = buttonSize,
        isStateOn = remember { mutableStateOf(false) }, //TODO - GET STATE FROM VIEWMODEL???
        enabled = enabled
    )
}

/**
 * Don't use the
 * @param[modifier.size()] for set button size - use
 * @param buttonSize instead
 */

@Composable
fun StatefulImageButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes buttonImageResStateOn: Int,
    @DrawableRes buttonImageResStateOff: Int,
    buttonSize: Dp,
    isStateOn: MutableState<Boolean>,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val unpressedButtonSize = buttonSize - 10.dp

    val calculatedButtonSize by animateDpAsState(
        targetValue = if (isPressed) buttonSize else unpressedButtonSize
    )

    val painter = if (isStateOn.value) {
        painterResource(id = buttonImageResStateOn)
    } else {
        painterResource(id = buttonImageResStateOff)
    }

    Box(modifier = modifier.size(buttonSize)) {
        IconButton(
            modifier = Modifier
                .align(Alignment.Center)
                .size(calculatedButtonSize),
            onClick = {
                isStateOn.value = !isStateOn.value
                onClick()
            },
            interactionSource = interactionSource,
            enabled = enabled
        ) {
            Image(
                painter = painter,
                contentDescription = null
            )
        }
    }
}


/**
 * Don't use the
 * @param[modifier.size()] for set button size - use
 * @param buttonSize instead
 */

@Composable
fun StatelessImageButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes pressedButtonImageRes: Int,
    @DrawableRes unpressedButtonImageRes: Int,
    buttonSize: Dp,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val painter = if (isPressed) {
        painterResource(id = pressedButtonImageRes)
    } else {
        painterResource(id = unpressedButtonImageRes)
    }

    val unpressedButtonSize = buttonSize - 10.dp

    val calculatedButtonSize by animateDpAsState(
        targetValue = if (isPressed) buttonSize else unpressedButtonSize
    )

    Box(modifier = modifier.size(buttonSize)) {
        IconButton(
            modifier = Modifier
                .align(Alignment.Center)
                .size(calculatedButtonSize),
            onClick = onClick,
            interactionSource = interactionSource,
            enabled = enabled
        ) {
            Image(
                painter = painter,
                contentDescription = null
            )
        }
    }
}