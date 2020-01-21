<template>
  <div class="top-middle-cmp">
    <div class="chart-name">
      10分钟投票增量统计曲线
      <dv-decoration-3 style="width:200px;height:20px;" />
    </div>
    <dv-charts :option="option" />
  </div>
</template>

<script>
export default {
  name: 'TopMiddleCmp',
  data () {
    return {
      option: {
        legend: {
          data: ['增量'],
          textStyle: {
            fill: '#fff'
          }
        },
        xAxis: {
          data: [
            '10/01', '10/02', '10/03', '10/04', '10/05', '10/06',
            '10/07', '10/07', '10/08', '10/09', '10/10', '10/11',
            '10/12', '10/13', '10/14', '10/15'
          ],
          boundaryGap: false,
          axisLine: {
            style: {
              stroke: '#999'
            }
          },
          axisLabel: {
            style: {
              fill: '#999'
            }
          },
          axisTick: {
            show: false
          }
        },
        yAxis: {
          data: 'value',
          splitLine: {
            show: false
          },
          axisLine: {
            style: {
              stroke: '#999'
            }
          },
          axisLabel: {
            style: {
              fill: '#999'
            },
            formatter ({ value }) {
              return value.toFixed(2)
            }
          },
          axisTick: {
            show: false
          },
          min: 95,
          max: 100,
          interval: 0.5
        },
        series: [
          {
            data: [
              99.56, 99.66, 99.84, 99.22, 99.11, 99.45,
              99.44, 99.81, 99.84, 99.32, 99.14, 99.45,
              99.15, 99.45, 99.64, 99.89
            ],
            type: 'line',
            name: '增量',
            smooth: true,
            lineArea: {
              show: true,
              gradient: ['rgba(55, 162, 218, 0.6)', 'rgba(55, 162, 218, 0)']
            },
            linePoint: {
              radius: 4,
              style: {
                fill: '#00db95'
              }
            }
          }
        ]
      }
    }
  },
  methods: {
    doPostCsdnTopNIncrement() {
      this.$http.post('http://localhost:8888/doPostCsdnTopNIncrement', {
        'name': 'aa'
      }).then(response=>{
        let result = response.data;
        let ticket = response.data.ticket;

        this.option = {
          legend: {
            data: result.name,
            textStyle: {
              fill: '#fff'
            },
            bottom :'8%'
          },
          xAxis: {
            data: result.date,
            boundaryGap: false,
            axisLine: {
              style: {
                stroke: '#999'
              }
            },
            axisLabel: {
              style: {
                fill: '#999'
              }
            },
            axisTick: {
              show: false
            }
          },
          yAxis: {
            data: 'value',
            splitLine: {
              show: false
            },
            axisLine: {
              style: {
                stroke: '#999'
              }
            },
            axisLabel: {
              style: {
                fill: '#999'
              },
              formatter ({ value }) {
                return value.toFixed(0)
              }
            },
            axisTick: {
              show: false
            },
            min: 0,
            max: 80,
            interval: 10
          },
          series: ticket
        }
      }).catch(function (error) {
        console.log(error)
      })
    }
  },
  mounted() {
    this.doPostCsdnTopNIncrement()
  }
}
</script>

<style lang="less">
.top-middle-cmp {
  position: relative;
  padding: 0 20px;
  box-sizing: border-box;

  height: 40%;
  display: flex;
  flex-grow: 0;
  box-sizing: border-box;
  padding-bottom: 20px;

  .chart-name {
    position: absolute;
    right: 70px;
    text-align: right;
    font-size: 20px;
    top: 10px;
  }
}

</style>
