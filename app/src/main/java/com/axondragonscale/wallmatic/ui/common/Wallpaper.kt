package com.axondragonscale.wallmatic.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Wallpaper(
    modifier: Modifier = Modifier,
    uri: String,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 16.dp,
    contentScale: ContentScale = ContentScale.Fit,
) {
    AsyncImage(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        model = uri,
        contentScale = contentScale,
        contentDescription = "",
    )
}
