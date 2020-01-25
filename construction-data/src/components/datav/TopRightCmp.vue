<template>
  <div class="top-right-cmp">
    <div class="chart-name">
      24小时得票数量统计曲线
      <dv-decoration-3 style="width:200px;height:20px;" />
    </div>
    <dv-charts :option="option" />
  </div>
</template>

<script>
export default {
  name: 'TopRightCmp',
  data () {
    return {
      option: {
        legend: {
          data: [
            {
              name: '天元浪子',
              color: '#00baff'
            },
            {
              name: '十步杀一人_千里不留行',
              color: '#ff5ca9'
            },
            {
              name: '沉默王二',
              color: '#3de7c9'
            },
            {
              name: '段智华',
              color: '#f5d94e'
            }
          ],
          textStyle: {
            fill: '#fff'
          }
        },
        xAxis: {
          data: [
            '10/01', '10/02', '10/03', '10/04', '10/05', '10/06', '10/07'
          ],
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
            }
          },
          axisTick: {
            show: false
          },
          min: 0,
          max: 8
        },
        series: [
          {
            name: '天元浪子',
            data: [
              2.5, 3.5, 6.5, 6.5, 7.5, 6.5, 2.5
            ],
            type: 'line',
            barStyle: {
              fill: 'rgba(0, 186, 255, 0.4)'
            }
          },
          {
            name: '十步杀一人_千里不留行',
            data: [
              2.5, 3.5, 6.5, 6.5, 7.5, 6.5, 2.5
            ],
            type: 'line',
            lineStyle: {
              stroke: '#ff5ca9'
            },
            linePoint: {
              radius: 4,
              style: {
                fill: '#ff5ca9',
                stroke: 'transparent'
              }
            }
          },
          {
            name: '沉默王二',
            data: [
              1.3, 2.3, 5.3, 5.3, 6.3, 5.3, 1.3
            ],
            type: 'line',
            smooth: true,
            lineArea: {
              show: true,
              gradient: ['rgba(55, 162, 218, 0.6)', 'rgba(55, 162, 218, 0)']
            },
            lineStyle: {
              lineDash: [5, 5]
            },
            linePoint: {
              radius: 4,
              style: {
                fill: '#00db95'
              }
            }
          },
          {
            data: [
              0.2, 1.2, 4.2, 4.2, 5.2, 4.2, 0.2
            ],
            type: 'line',
            name: '段智华',
            lineArea: {
              show: true,
              gradient: ['rgba(245, 217, 79, 0.8)', 'rgba(245, 217, 79, 0.2)']
            },
            lineStyle: {
              stroke: '#f5d94e'
            },
            linePoint: {
              radius: 4,
              style: {
                fill: '#f5d94e',
                stroke: 'transparent'
              }
            }
          }
        ]
      }
    }
  },
  methods: {
    doPostCsdnTop20() {
      this.$http.post('/doPostCsdnTop20', {
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
              }
            },
            axisTick: {
              show: false
            },
            min: ticket[9].data[0],
            max: ticket[0].data[23]
          },
          series: ticket
        }
      }).catch(function (error) {
        console.log(error)
      })
    }
  },
  mounted() {
    this.doPostCsdnTop20()
  }
}
</script>

<style lang="less">
.top-right-cmp {
  position: relative;
  padding: 0 20px;
  box-sizing: border-box;
  height: 40%;
  .chart-name {
    position: absolute;
    right: 70px;
    text-align: right;
    font-size: 20px;
    top: 10px;
  }
}
</style>
