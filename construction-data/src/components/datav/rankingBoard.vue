<template>
  <div id="ranking-board">
    <div class="ranking-board-title">日增投票数量柱状图</div>
    <dv-scroll-ranking-board :config="config"/>
  </div>
</template>

<script>
  export default {
    name: 'RankingBoard',
    data() {
      return {
        config: {
          data: [
            {
              name: '天元浪子', value: 2506
            }
          ],
          rowNum: 20
        }
      }
    },
    methods: {
      getRankingBoard() {
        this.$http.post('/getRankingBoard', {
          'name': 'aa'
        }).then(response=>{
          this.config = { data: response.data ,rowNum:22}
        }).catch(function (error) {
          console.log(error)
        })
      }
    },
    mounted() {
      this.getRankingBoard()
    }
  }
</script>

<style lang="less">
  #ranking-board {
    width: 25%;
    height: 90%;
    box-shadow: 0 0 3px blue;
    display: flex;
    flex-direction: column;
    background-color: rgba(6, 30, 93, 0.5);
    border-top: 2px solid rgba(1, 153, 209, .5);
    box-sizing: border-box;
    padding: 0px 30px;

    .ranking-board-title {
      font-weight: bold;
      height: 50px;
      display: flex;
      align-items: center;
      font-size: 20px;
    }

    .dv-scroll-ranking-board {
      flex: 1;
    }
  }
</style>
