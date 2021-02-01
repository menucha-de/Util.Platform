CC ?= gcc
CFLAGS=-std=c99 -Iinclude -I$(JDK_INCLUDE) -I$(JDK_INCLUDE)/linux -I$(INCLUDE) -O3 -Wall -fmessage-length=0 -fPIC -MMD -MP
INCLUDE=target/include
LDFLAGS=-shared
SOURCES=src/main/linux/havis_util_platform_impl_LinuxPlatform.c
TARGET=target/classes/liblinuxplatform.so
OBJS=$(SOURCES:.c=.o)

ifeq ($(shell uname -m), armv7l)
	ARCH ?= armhf
else
	ARCH ?= amd64
endif

JDK_INCLUDE=$(JAVA_HOME)/include

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) $(CFLAGS) -o $@ $(OBJS)

clean:
	rm -f $(OBJS) $(TARGET) src/*.d
