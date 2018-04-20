#
# Include this make file to build your application against this module.
#
# Make sure to include it after you've set all your desired LOCAL variables.
# Note that you must explicitly set your LOCAL_RESOURCE_DIR before including
# this file.
#
# For example:
#
#   LOCAL_RESOURCE_DIR := \
#        $(LOCAL_PATH)/res
#
#   include frameworks/base/packages/SettingsLib/common.mk
#

LOCAL_STATIC_JAVA_LIBRARIES += \
    androidx.annotation_annotation \
    androidx.lifecycle_lifecycle-common

LOCAL_STATIC_ANDROID_LIBRARIES += \
    androidx.legacy_legacy-support-v4 \
    androidx.lifecycle_lifecycle-runtime \
    androidx.recyclerview_recyclerview \
    androidx.preference_preference \
    androidx.appcompat_appcompat \
    androidx.legacy_legacy-preference-v14 \
    SettingsLib

