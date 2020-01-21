<template>
  <div id="digital-flop">
    <div
      class="digital-flop-item"
      v-for="item in digitalFlopData"
      :key="item.title"
    >
      <div class="digital-flop-title">{{ item.title }}</div>
      <div class="digital-flop">
        <dv-digital-flop
          :config="item.number"
          style="width:100px;height:50px;"
        />
          <div class="unit">{{ item.unit }}</div>
      </div>
    </div>

    <dv-decoration-10 />
  </div>
</template>

<script>
export default {
  name: 'DigitalFlop',
  data () {
    return {
      digitalFlopData: []
    }
  },
  methods: {
    createData () {
      this.$http.post('http://localhost:8888/DigitalFlop', {
      }).then(response=>{
        this.digitalFlopData = [
          {
            title: '参与博主',
            number: {
              number: [response.data.blogernum],
              content: '{nt}',
              textAlign: 'center',
              style: {
                fill: '#4d99fc',
                fontWeight: 'bold'
              }
            },
            unit: '人'
          },
          {
            title: '累计投票',
            number: {
              number: [response.data.allvotenum],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#f46827',
                fontWeight: 'bold'
              }
            },
            unit: '票'
          },
          {
            title: '访问次数',
            number: {
              number: [response.data.allviewnum],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#40faee',
                fontWeight: 'bold'
              }
            },
            unit: '次'
          }
        ]
      }).catch(function (error) {
        console.log(error)
      })
    }
  },
  mounted () {
    const { createData } = this
    createData()
    setInterval(createData, 60000)
  }
}
</script>

<style lang="less">
#digital-flop {
  position: relative;
  height: 15%;
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(6, 30, 93, 0.5);

  .dv-decoration-10 {
    position: absolute;
    width: 95%;
    left: 2.5%;
    height: 5px;
    bottom: 0px;
  }

  .digital-flop-item {
    width: 25%;
    height: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-left: 3px solid rgb(6, 30, 93);
    border-right: 3px solid rgb(6, 30, 93);
  }

  .digital-flop-title {
    font-size: 20px;
    margin-bottom: 20px;
  }

  .digital-flop {
    display: flex;
  }

  .unit {
    margin-left: 10px;
    display: flex;
    align-items: flex-end;
    box-sizing: border-box;
    padding-bottom: 13px;
  }
}
</style>
