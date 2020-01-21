<template>
  <div id="scroll-board">
    <!--<div class="scroll-board-title">TOP20位次变化统计表</div>-->
    <dv-scroll-board :config="config"/>
  </div>
</template>

<script>
  export default {
    name: 'ScrollBoard',
    data() {
      return {
        config: {
          header: ['编号', '博主', '升降', '总票数', '日增票数'],
          data: [
            ['168', '天元浪子', '-', '11456', '1686']
          ],
          index: true,
          columnWidth: [50, 80, 200, 100, 120],
          align: ['center'],
          rowNum: 20,
          headerBGC: '#1981f6',
          headerHeight: 45,
          oddRowBGC: 'rgba(0, 44, 81, 0.8)',
          evenRowBGC: 'rgba(10, 29, 50, 0.8)'
        }
      }
    },
    methods: {
      getScrollBoard() {
        this.$http.post('/getScrollBoard', {}).then(response => {
          this.config = {
            header: ['编号', '博主', '升降', '总票数', '最新统计时间'],
            data: response.data,
            index: true,
            columnWidth: [30, 50, 170, 50, 100],
            align: ['center'],
            rowNum: 22,
            headerBGC: '#1981f6',
            headerHeight: 45,
            oddRowBGC: 'rgba(0, 44, 81, 0.8)',
            evenRowBGC: 'rgba(10, 29, 50, 0.8)'
          }
        }).catch(function (error) {
          console.log(error)
        })
      }
    },
    mounted() {
      this.getScrollBoard()
    }
  }
</script>

<style lang="less">


  #scroll-board {
    width: 35%;
    height: 90%;
    box-sizing: border-box;
    margin-left: 20px;
    overflow: hidden;
    .scroll-board-title {
      font-weight: bold;
      height: 50px;
      display: flex;
      align-items: center;
      font-size: 20px;
    }
  }
</style>
