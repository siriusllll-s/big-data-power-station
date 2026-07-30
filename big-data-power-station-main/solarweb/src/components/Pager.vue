<template>
  <div class="pager-box">
    <span>共 {{totalItemCount}} 条</span>
    <span>每页</span>
    <select v-model="pageSizeLocal" @change="changePageSize">
      <option :value="5">5</option>
      <option :value="10">10</option>
      <option :value="20">20</option>
      <option :value="50">50</option>
    </select>
    <span>条</span>
    <button :disabled="pageIndexLocal <= 1" @click="changePage(pageIndexLocal - 1)">上一页</button>
    <span>第 {{pageIndexLocal}} / {{pageTotal}} 页</span>
    <button :disabled="pageIndexLocal >= pageTotal" @click="changePage(pageIndexLocal + 1)">下一页</button>
  </div>
</template>

<script>
export default {
  name: "Pager",
  props: {
    pageIndex: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 5
    },
    totalItemCount: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      pageIndexLocal: this.pageIndex,
      pageSizeLocal: this.pageSize
    }
  },
  computed: {
    pageTotal() {
      if (!this.totalItemCount) return 1;
      return Math.ceil(this.totalItemCount / this.pageSizeLocal);
    }
  },
  watch: {
    pageIndex(v) {
      this.pageIndexLocal = v;
    },
    pageSize(v) {
      this.pageSizeLocal = v;
    }
  },
  methods: {
    changePage(page) {
      if (page < 1) page = 1;
      if (page > this.pageTotal) page = this.pageTotal;
      this.pageIndexLocal = page;
      this.$emit('update:pageIndex', this.pageIndexLocal);
      this.$emit('change');
    },
    changePageSize() {
      this.pageIndexLocal = 1;
      this.$emit('update:pageSize', this.pageSizeLocal);
      this.$emit('update:pageIndex', 1);
      this.$emit('change');
    }
  }
};
</script>

<style scoped>
.pager-box {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 12px 8px;
  gap: 8px;
}
.pager-box button {
  padding: 4px 10px;
  border: 1px solid #dcdfe6;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  color: #606266;
}
.pager-box button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.pager-box select {
  padding: 3px 6px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}
</style>
