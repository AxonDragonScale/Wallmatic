package com.axondragonscale.wallmatic.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Wallpaper(
    modifier: Modifier = Modifier,
    uri: String,
    cornerRadius: Dp = 16.dp,
) {
    AsyncImage(
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
        model = uri,
        contentDescription = ""
    )
}
