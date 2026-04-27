# WalkMate ProGuard rules
# 일반적으로 AGP 기본 rules + 라이브러리 consumer rules로 충분.
# 아래 규칙은 Week 7 Material 마감 단계에서 검토.

# Moshi: @JsonClass(generateAdapter = true) DTO 클래스 보존
-keep,allowobfuscation,allowshrinking class com.walkmate.data.remote.dto.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*

# Hilt 자동 처리
# Room 자동 처리

# Health Connect
-keep class androidx.health.** { *; }

# Crash 리포트 시 라인 번호 보존 (선택)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
