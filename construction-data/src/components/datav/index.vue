<template>
  <div id="data-view">
    <dv-full-screen-container>

      <top-header/>

      <div class="main-content">
        <digital-flop/>

        <div class="block-left-right-content">
          <ranking-board/>

          <div class="block-top-bottom-content">
            <top-middle-cmp/>
            <top-right-cmp/>
          </div>
          <scroll-board/>
        </div>
      </div>
    </dv-full-screen-container>
  </div>
</template>

<script>
  import topHeader from './topHeader'
  import digitalFlop from './digitalFlop'
  import rankingBoard from './rankingBoard'
  import roseChart from './roseChart'
  import waterLevelChart from './waterLevelChart'
  import scrollBoard from './scrollBoard'
  import cards from './cards'
  import TopMiddleCmp from './TopMiddleCmp'
  import TopRightCmp from './TopRightCmp'

  export default {
    name: 'DataView',
    components: {
      topHeader,
      digitalFlop,
      rankingBoard,
      roseChart,
      waterLevelChart,
      scrollBoard,
      cards,
      TopMiddleCmp,
      TopRightCmp
    },
    data() {
      return {}
    },
    methods: {}
  }
</script>

<style lang="less">
  #data-view {
    width: 100%;
    height: 100%;
    background-color: #030409;
    color: #fff;

    #dv-full-screen-container {
      background-image: url('./img/bg.png');
      background-size: 100% 100%;
      box-shadow: 0 0 3px blue;
      display: flex;
      flex-direction: column;
    }

    .main-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      .top-middle-cmp, .top-right-cmp {
        width: 100%;
      }
    }

    .block-left-right-content {
      flex: 1;
      display: flex;
      margin-top: 20px;
    }

    .block-top-bottom-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding-left: 20px;
    }

    .block-top-content {
      height: 40%;
      display: flex;
      flex-grow: 0;
      box-sizing: border-box;
      padding-bottom: 20px;
    }
  }
</style>
