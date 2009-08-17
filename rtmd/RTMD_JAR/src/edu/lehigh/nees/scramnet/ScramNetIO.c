#include <jni.h>
#include <stdio.h>
#include <ctype.h>
#include <scrplus.h>
#include "edu_lehigh_nees_scramnet_ScramNetIO.h"

/* This is just a wrapper around the memory mapping routine */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_initScramnet(JNIEnv *env, jobject obj)
{
  int scramInit; //used for memory and control mapping

  //initalize Scramnet
  if ((scramInit = sp_scram_init()) == 0) 
    printf("SCRAMNet Initialization: SUCCESS!\n"); 
  else {
    printf("SCRAMNet Initialization: FAILED!\nError Code: %i\n",scramInit); 
    return 1;
  }

  // Set mode to long
  sp_stm_mm(0);

  // Default CSRs
  scr_csr_write(SCR_CSR0,0x8003);  
  scr_csr_write(SCR_CSR2,0xC040); 
  
  return scramInit;
}

/* Set the transaction mode */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_setTransMode(JNIEnv *env, jobject obj, jint mode)
{
  return (sp_stm_mm(mode));
}

/* This is just a wrapper around the memory unmapping routines */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_unmapScramnet(JNIEnv *env, jobject obj)
{
  scr_mem_mm(UNMAP);
  scr_reg_mm(UNMAP);
  printf("SCRAMNet successfully unmapped!\n");
  return 0;
}


/**********************************************
 *  READ ROUTINES
 *********************************************/

/* Read an int from a particular address location, loc */
JNIEXPORT jbyte JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_readByte(JNIEnv *env, jobject obj, jint loc)
{
  // Return the value at that location
  return ((volatile byte *)get_base_mem())[loc];
}

/* Read an int from a particular address location, loc */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_readInt(JNIEnv *env, jobject obj, jint loc)
{
  // Return the value at that location
  return ((volatile int *)get_base_mem())[loc];
}

/* Read a long from a particular address location, loc */
JNIEXPORT jlong JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_readLong(JNIEnv *env, jobject obj, jint loc)
{
  // Return the value at that location
  return ((volatile long *)get_base_mem())[loc];
}

/* Read a float from a particular address location, loc */
JNIEXPORT jfloat JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_readFloat(JNIEnv *env, jobject obj, jint loc)
{
  // Return the value at that location
  return ((volatile float *)get_base_mem())[loc];
}

/* Read a double from a particular address location, loc */
JNIEXPORT jdouble JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_readDouble(JNIEnv *env, jobject obj, jint loc)
{
  // Return the value at that location
  return ((volatile double *)get_base_mem())[loc];
}


/**********************************************
 *  WRITE ROUTINES
 *********************************************/

/* Write an int to a particular address location, loc */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_writeInt(JNIEnv *env, jobject obj, jint loc, jint value)
{
  volatile int *addr;

  // Get the base address location and cast as float
  addr = (volatile int *)get_base_mem();

  // Write value to address
  addr[loc] = value;

  return 0;
}

/* Write a long to a particular address location, loc */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_writeLong(JNIEnv *env, jobject obj, jint loc, jlong value)
{
  volatile long *addr;

  // Get the base address location and cast as long
  addr = (volatile long *)get_base_mem();

  // Write value to address
  addr[loc] = value;

  return 0;
}

/* Write a float to a particular address location, loc */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_writeFloat(JNIEnv *env, jobject obj, jint loc, jfloat value)
{
  volatile float *addr;

  // Get the base address location and cast as float
  addr = (volatile float *)get_base_mem();

  // Write value to address
  addr[loc] = value;

  return 0;
}

/* Write a double to a particular address location, loc */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_writeDouble(JNIEnv *env, jobject obj, jint loc, jdouble value)
{
  volatile double *addr;

  // Get the base address location and cast as double
  addr = (volatile double *)get_base_mem();

  // Write value to address
  addr[loc] = value;

  return 0;
}



/**********************************************
 *  Utilities
 *********************************************/

/* Clear SCRAMNet memory */
JNIEXPORT jint JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_clear(JNIEnv *env, jobject obj)
{
  scr_mclr_mm(MEM);
  return 0;
}

/* Read Servotest Pulsar DSP Global Counter */
JNIEXPORT jlong JNICALL Java_edu_lehigh_nees_scramnet_ScramNetIO_readGlobalCounter(JNIEnv *env, jobject obj)
{
  // Return the value
  return (get_base_mem()[64]);
}