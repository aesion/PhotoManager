package com.pulamsi.photomanager.bean;

import java.io.File;
import java.io.Serializable;

public class SerializableData implements Serializable {
        private File[] files;

        public File[] getFiles() {
            return files;
        }

        public void setFiles(File[] files) {
            this.files = files;
        }
    }