<template>
  <el-dialog
    :title="'上传电站图片 - ' + name"
    :visible.sync="dialogVisible"
    width="45%"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    append-to-body
  >
    <div v-loading="uploading">
      <el-upload
        class="avatar-uploader"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError"
        accept="image/*"
      >
        <img v-if="previewUrl" :src="previewUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <div class="tip">
        <span>支持 jpg、png 格式，单张图片 ≤ 5MB</span>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" :disabled="!photoKey" :loading="saving" @click="save">保存</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { savePhoto, upload as uploadApi } from '@/api/Station/Station'

function getToken () {
  return localStorage.getItem('Authorization') || ''
}

export default {
  data() {
    return {
      dialogVisible: false,
      id: 1,
      name: '',
      uploading: false,
      saving: false,
      previewUrl: '',
      photoKey: ''
    }
  },
  computed: {
    uploadUrl() {
      const base = (process.env.NODE_ENV === 'development' ? '/api' : (process.env.VUE_APP_BASE_API || ''))
      return base + '/minio/upload'
    },
    uploadHeaders() {
      return {
        'Authorization': getToken()
      }
    }
  },
  methods: {
    showDialog(id, name) {
      this.id = id || 1
      this.name = name
      this.previewUrl = ''
      this.photoKey = ''
      this.dialogVisible = true
    },
    beforeUpload(file) {
      const isImg = /image\/(jpeg|png|jpg)/.test(file.type)
      const lt5M = file.size / 1024 / 1024 < 5
      if (!isImg) {
        this.$message.error('仅支持 JPG/PNG 图片格式!')
        return false
      }
      if (!lt5M) {
        this.$message.error('图片大小不能超过 5MB!')
        return false
      }
      this.uploading = true
      return true
    },
    handleSuccess(response, file) {
      this.uploading = false
      if (response && (response.successful !== false)) {
        const result = response.resultValue || response.data || response
        const key = typeof result === 'string' ? result : (result.key || result.path || result.name || result.url)
        this.photoKey = key
        const reader = new FileReader()
        reader.onload = e => { this.previewUrl = e.target.result }
        reader.readAsDataURL(file)
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.resultHint || '上传失败')
      }
    },
    handleError(err) {
      this.uploading = false
      console.error(err)
      this.$message.error('上传失败，请稍后重试')
    },
    save() {
      if (!this.photoKey) return
      this.saving = true
      savePhoto({ id: this.id, photoPath: this.photoKey }).then(res => {
        if (res.successful) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.$emit('close')
        } else {
          this.$message.error(res.resultHint || '保存失败')
        }
        this.saving = false
      }).catch(() => { this.saving = false })
    }
  }
}
</script>

<style scoped>
.avatar-uploader >>> .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 280px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-uploader >>> .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
.avatar {
  max-width: 280px;
  max-height: 200px;
  display: block;
}
.tip {
  color: #909399;
  font-size: 12px;
  margin-top: 8px;
}
</style>
