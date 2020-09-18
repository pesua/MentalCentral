import { Component, OnInit, Inject, LOCALE_ID } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AlignmentType, Document, Packer, Paragraph, TabStopPosition, TabStopType, TextRun } from 'docx';

import { saveAs } from 'file-saver';

import { IVisit } from 'app/shared/model/visit.model';
import { UserExtraService } from '../user-extra/user-extra.service';
import { formatDate } from '@angular/common';

@Component({
  selector: 'jhi-visit-detail',
  templateUrl: './visit-detail.component.html',
})
export class VisitDetailComponent implements OnInit {
  now = new Date();
  nowFormatted: string;
  visit: IVisit | null = null;

  constructor(@Inject(LOCALE_ID) locale: string, protected activatedRoute: ActivatedRoute, private userExtraService: UserExtraService) {
    this.nowFormatted = formatDate(this.now, 'dd.MM.yyyy', locale);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => (this.visit = visit));

    this.userExtraService.find(this.visit!.user!.id).subscribe(userExtra => {
      this.visit!.user!.middleName = userExtra.body!.middleName;
      this.visit!.user!.phoneNumber = userExtra.body!.phoneNumber;
      this.visit!.user!.degree = userExtra.body!.degree;
    });
  }

  previousState(): void {
    window.history.back();
  }

  creatingAndDownloadingPDF(): void {
    const doc = new Document();

    doc.addSection({
      children: [
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: {
            after: 50,
          },
          children: [
            new TextRun({
              text: 'Обласная клиническая больница им. И.И. Мечникова',
              size: 30,
              bold: true,
            }),
          ],
        }),
        new Paragraph({
          tabStops: [
            {
              type: TabStopType.RIGHT,
              position: TabStopPosition.MAX,
            },
          ],
          children: [
            new TextRun({
              text: 'Тел 0988944992, 0991582275',
            }),
            new TextRun({
              text: '\tУкраина, 49005',
            }),
          ],
        }),
        new Paragraph({
          tabStops: [
            {
              type: TabStopType.RIGHT,
              position: TabStopPosition.MAX,
            },
          ],
          children: [
            new TextRun({
              text: 'Факс: (0562) 36-09-67',
            }),
            new TextRun({
              text: '\tг. Днепр',
            }),
          ],
        }),
        new Paragraph({
          spacing: {
            after: 50,
          },
          tabStops: [
            {
              type: TabStopType.RIGHT,
              position: TabStopPosition.MAX,
            },
          ],
          children: [
            new TextRun({
              text: 'e-mail: psookbm@gmail.com',
            }),
            new TextRun({
              text: '\tпл. Соборная, 14',
            }),
          ],
        }),
        new Paragraph({
          text:
            'Консультировал врач ' + this.visit!.user!.lastName + ' ' + this.visit!.user!.firstName + ' (' + this.visit!.user!.degree + ')',
          alignment: AlignmentType.CENTER,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Б-ой: ' + this.visit!.patient!.fullName,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Прож: ' + this.visit!.patient!.address,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Д-з: ' + this.visit!.patient!.diagnosis,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Рекомендовано: ' + this.visit!.therapy,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: this.nowFormatted,
          spacing: {
            after: 50,
          },
        }),
      ],
    });

    Packer.toBlob(doc).then(blob => {
      saveAs(blob, this.visit!.patient!.fullName + '.docx');
    });
  }
}
