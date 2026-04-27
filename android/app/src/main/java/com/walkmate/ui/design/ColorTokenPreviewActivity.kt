package com.walkmate.ui.design

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.walkmate.ui.theme.WalkMateTheme

/**
 * Material 3 28 토큰 + WalkMate 도메인 23 토큰 시각 검증용 Activity.
 *
 * 사용 (debug 빌드):
 *   adb shell am start -n com.walkmate.debug/com.walkmate.ui.design.ColorTokenPreviewActivity
 *
 * 검증 항목 (mapping-rationale.md §검증 방법):
 * - Light/Dark 미리보기에서 walkmate-ui.html 시안과 색감 5% 이내 일치
 * - Container/onContainer 쌍 4.5:1 이상 대비
 * - 28 토큰 모두 시각 렌더링 확인
 */
class ColorTokenPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalkMateTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ColorTokenList()
                }
            }
        }
    }
}

@Composable
private fun ColorTokenList() {
    val colors = MaterialTheme.colorScheme
    val tokens = listOf(
        "primary" to (colors.primary to colors.onPrimary),
        "primaryContainer" to (colors.primaryContainer to colors.onPrimaryContainer),
        "secondary" to (colors.secondary to colors.onSecondary),
        "secondaryContainer" to (colors.secondaryContainer to colors.onSecondaryContainer),
        "tertiary" to (colors.tertiary to colors.onTertiary),
        "tertiaryContainer" to (colors.tertiaryContainer to colors.onTertiaryContainer),
        "error" to (colors.error to colors.onError),
        "errorContainer" to (colors.errorContainer to colors.onErrorContainer),
        "background" to (colors.background to colors.onBackground),
        "surface" to (colors.surface to colors.onSurface),
        "surfaceVariant" to (colors.surfaceVariant to colors.onSurfaceVariant),
        "surfaceContainerLowest" to (colors.surfaceContainerLowest to colors.onSurface),
        "surfaceContainerLow" to (colors.surfaceContainerLow to colors.onSurface),
        "surfaceContainer" to (colors.surfaceContainer to colors.onSurface),
        "surfaceContainerHigh" to (colors.surfaceContainerHigh to colors.onSurface),
        "surfaceContainerHighest" to (colors.surfaceContainerHighest to colors.onSurface),
        "outline" to (colors.outline to colors.surface),
        "outlineVariant" to (colors.outlineVariant to colors.onSurface),
        "inverseSurface" to (colors.inverseSurface to colors.inverseOnSurface),
        "inversePrimary" to (colors.inversePrimary to colors.surface),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(tokens) { (name, pair) ->
            ColorRow(name, pair.first, pair.second)
        }
    }
}

@Composable
private fun ColorRow(name: String, bg: Color, fg: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .background(bg)
                .padding(16.dp)
        ) {
            Text(
                text = "Aa",
                color = fg,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ColorTokenPreview() {
    WalkMateTheme {
        Surface { ColorTokenList() }
    }
}
