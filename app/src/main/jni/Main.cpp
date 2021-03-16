
#include <list>
#include <vector>
#include <string.h>
#include <GLES2/gl2.h>
#include <pthread.h>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include "Includes/obfuscate.h"
#include "KittyMemory/MemoryPatch.h"
#include "Includes/Logger.h"
#include "Includes/Utils.h"
#include "Menu.h"
#include "Toast.h"
#include <And64InlineHook/And64InlineHook.hpp>
#include <chrono>
#include <pthread.h>
#include <jni.h>
#include <GLES2/gl2.h>
#include <dlfcn.h>
#include "KittyMemory/MemoryPatch.h"
#include "Includes/Logger.h"
#include "Includes/Utils.h"
#include <pthread.h>
#include <jni.h>
#include "KittyMemory/MemoryPatch.h"
#include "Includes/Logger.h"
#include <GLES2/gl2.h>
#include <dlfcn.h>
#include "KittyMemory/MemoryPatch.h"
#include "Includes/Logger.h"
#include "Includes/Utils.h"

#define InitUFunc(x, y) *reinterpret_cast<void **>(&x) = IL2CppCallResolver::ResolveCall(y)

#if defined(__aarch64__)
#else

#include <Substrate/SubstrateHook.h>
#include <Substrate/CydiaSubstrate.h>

#endif


struct My_Patches {

    MemoryPatch TestAlvinHacker, TestIseStudio;

} hexPatches;

bool feature1, feature2;







#define targetLibName OBFUSCATE("libil2cpp.so")

extern "C" {
JNIEXPORT jobjectArray
JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList(JNIEnv *env, jobject activityObject) {
    jobjectArray ret;

    const char *features[] = {
            OBFUSCATE("0_Category_Server"),
            OBFUSCATE("1_ButtonOnOff_Test Alvin Hacker"),
            OBFUSCATE("2_ButtonOnOff_Test Ise Studio"),

    };

    int Total_Feature = (sizeof features /
                         sizeof features[0]);
    ret = (jobjectArray)
            env->NewObjectArray(Total_Feature, env->FindClass(OBFUSCATE("java/lang/String")),
                                env->NewStringUTF(""));
    int i;
    for (i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));

    pthread_t ptid;
    pthread_create(&ptid, NULL, antiLeech, NULL);

    return (ret);
}
JNIEXPORT void JNICALL
Java_uk_lgl_modmenu_Preferences_Changes(JNIEnv *env, jclass clazz, jobject obj,
                                        jint feature, jint value, jboolean boolean, jstring str) {

    const char *featureName = env->GetStringUTFChars(str, 0);

    LOGD(OBFUSCATE("Feature name: %d - %s | Value: = %d | Bool: = %d"), feature, featureName, value,
         boolean);

    switch (feature) {
        case 1:
            feature1 = boolean;
            if (feature1) {
                hexPatches.TestAlvinHacker.Modify();
                LOGI(OBFUSCATE("On"));
            } else {
                hexPatches.TestAlvinHacker.Restore();
                LOGI(OBFUSCATE("Off"));
            }
            break;
        case 2:
            feature2 = boolean;
            if (feature2) {
                hexPatches.TestIseStudio.Modify();
                LOGI(OBFUSCATE("On"));
            } else {
                hexPatches.TestIseStudio.Restore();
                LOGI(OBFUSCATE("Off"));
            }
            break;
    }
}
}

void *hack_thread(void *) {
    LOGI(OBFUSCATE("pthread called"));


    do {
        sleep(1);
    } while (!isLibraryLoaded(targetLibName));

    LOGI(OBFUSCATE("%s has been loaded"), (const char *) targetLibName);

#if defined(__aarch64__)



#else
    hexPatches.TestAlvinHacker = MemoryPatch::createWithHex(targetLibName,
                                                     string2Offset(OBFUSCATE_KEY("0x5454432", '-')),
                                                     OBFUSCATE("00 00 A0 E3 1E FF 2F E1"));

    hexPatches.TestIseStudio = MemoryPatch::createWithHex(targetLibName,
                                                         string2Offset(OBFUSCATE_KEY("0x5454432", '-')),
                                                         OBFUSCATE("00 00 A0 E3 1E FF 2F E1"));


    LOGI(OBFUSCATE("Done"));
#endif

    return NULL;
}


__attribute__((constructor))
void lib_main() {
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);
}


