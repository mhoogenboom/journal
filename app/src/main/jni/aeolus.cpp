#include <cstddef>
#include <jni.h>
#include <android/log.h>
#include "generator.h"

extern "C" {
    void unmarshallInput(JNIEnv *env, jobject jposition, Square square[])
    {
        jclass moveClass = env->FindClass("com/robinfinch/journal/domain/draughts/Position");

        jmethodID getSquaresMethod = env->GetMethodID(moveClass, "getSquares", "()[I");

        jintArray jsquare = (jintArray) env->CallObjectMethod(jposition, getSquaresMethod);

        int squareAsIntArray [NUMBER_OF_SQUARES + 1];
        env->GetIntArrayRegion(jsquare, 0, NUMBER_OF_SQUARES + 1, squareAsIntArray);

        for (int number = 0; number <= NUMBER_OF_SQUARES; number++)
        {
            square[number].number = number;
            square[number].empty = (squareAsIntArray[number] & 1);
            square[number].white = (squareAsIntArray[number] & 2);
            square[number].king = (squareAsIntArray[number] & 4);
        }
    }

    jobjectArray marshallOutput(JNIEnv *env, std::list< std::list<int> > * paths)
    {
        jclass moveClass = env->FindClass("com/robinfinch/journal/domain/draughts/Move");

        jmethodID moveCtor = env->GetMethodID(moveClass, "<init>", "([I)V");

        jobjectArray jmoves = env->NewObjectArray(paths->size(), moveClass, NULL);

        int i = 0;
        for (auto path = paths->begin(); path != paths->end(); path++)
        {
            int pathAsIntArray [path->size()];

            int j = 0;
            for (auto intElement = path->begin(); intElement != path->end(); intElement++) {
                pathAsIntArray[j++] = *intElement;
            }

            jintArray jpath = env->NewIntArray(path->size());

            env->SetIntArrayRegion(jpath, 0, path->size(), pathAsIntArray);

            jobject jmove = env->NewObject(moveClass, moveCtor, jpath);

            env->SetObjectArrayElement(jmoves, i++, jmove);
        }

        return jmoves;
    }

    jobjectArray Java_com_robinfinch_journal_app_draughts_Engine_generate(
         JNIEnv *env,
         jobject obj,
         jobject jposition)
    {
        __android_log_print(ANDROID_LOG_DEBUG, "com.robinfinch.journal.log", "Generate moves for position");

        Square square [NUMBER_OF_SQUARES + 1];

        square[0].init(&square[0], &square[0], &square[0], &square[0]);
        square[1].init(&square[0], &square[0], &square[7], &square[6]);
        square[2].init(&square[0], &square[0], &square[8], &square[7]);
        square[3].init(&square[0], &square[0], &square[9], &square[8]);
        square[4].init(&square[0], &square[0], &square[10], &square[9]);
        square[5].init(&square[0], &square[0], &square[0], &square[10]);
        square[6].init(&square[0], &square[1], &square[11], &square[0]);
        square[7].init(&square[1], &square[2], &square[12], &square[11]);
        square[8].init(&square[2], &square[3], &square[13], &square[12]);
        square[9].init(&square[3], &square[4], &square[14], &square[13]);
        square[10].init(&square[4], &square[5], &square[15], &square[14]);
        square[11].init(&square[6], &square[7], &square[17], &square[16]);
        square[12].init(&square[7], &square[8], &square[18], &square[17]);
        square[13].init(&square[8], &square[9], &square[19], &square[18]);
        square[14].init(&square[9], &square[10], &square[20], &square[19]);
        square[15].init(&square[10], &square[0], &square[0], &square[20]);
        square[16].init(&square[0], &square[11], &square[21], &square[0]);
        square[17].init(&square[11], &square[12], &square[22], &square[21]);
        square[18].init(&square[12], &square[13], &square[23], &square[22]);
        square[19].init(&square[13], &square[14], &square[24], &square[23]);
        square[20].init(&square[14], &square[15], &square[25], &square[24]);
        square[21].init(&square[16], &square[17], &square[27], &square[26]);
        square[22].init(&square[17], &square[18], &square[28], &square[27]);
        square[23].init(&square[18], &square[19], &square[29], &square[28]);
        square[24].init(&square[19], &square[20], &square[30], &square[29]);
        square[25].init(&square[20], &square[0], &square[0], &square[30]);
        square[26].init(&square[0], &square[21], &square[31], &square[0]);
        square[27].init(&square[21], &square[22], &square[32], &square[31]);
        square[28].init(&square[22], &square[23], &square[33], &square[32]);
        square[29].init(&square[23], &square[24], &square[34], &square[33]);
        square[30].init(&square[24], &square[25], &square[35], &square[34]);
        square[31].init(&square[26], &square[27], &square[37], &square[36]);
        square[32].init(&square[27], &square[28], &square[38], &square[37]);
        square[33].init(&square[28], &square[29], &square[39], &square[38]);
        square[34].init(&square[29], &square[30], &square[40], &square[39]);
        square[35].init(&square[30], &square[0], &square[0], &square[40]);
        square[36].init(&square[0], &square[31], &square[41], &square[0]);
        square[37].init(&square[31], &square[32], &square[42], &square[41]);
        square[38].init(&square[32], &square[33], &square[43], &square[42]);
        square[39].init(&square[33], &square[34], &square[44], &square[43]);
        square[40].init(&square[34], &square[35], &square[45], &square[44]);
        square[41].init(&square[36], &square[37], &square[47], &square[46]);
        square[42].init(&square[37], &square[38], &square[48], &square[47]);
        square[43].init(&square[38], &square[39], &square[49], &square[48]);
        square[44].init(&square[39], &square[40], &square[50], &square[49]);
        square[45].init(&square[40], &square[0], &square[0], &square[50]);
        square[46].init(&square[0], &square[41], &square[0], &square[0]);
        square[47].init(&square[41], &square[42], &square[0], &square[0]);
        square[48].init(&square[42], &square[43], &square[0], &square[0]);
        square[49].init(&square[43], &square[44], &square[0], &square[0]);
        square[50].init(&square[44], &square[45], &square[0], &square[0]);

        unmarshallInput(env, jposition, square);

        Generator generator;
        std::list< std::list<int> > * paths = generator.generatePaths(square);

        return marshallOutput(env, paths);
    }
}