#include "havis_util_platform_impl_LinuxPlatform.h"

#include <jni.h>
#include <sys/sysinfo.h>

JNIEXPORT jlong JNICALL Java_havis_util_platform_impl_LinuxPlatform_getUptime(
		JNIEnv *env, jclass clazz) {
	struct sysinfo s_info;
	return sysinfo(&s_info) == 0 ? s_info.uptime * 1000 : -1;
}
